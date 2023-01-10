package com.unimerch.service.analytics;

import com.unimerch.dto.analytics.AmznFilter;
import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.analytics.DateFilter;
import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.dto.tag.TagGroupTagResult;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.mapper.ProductMapper;
import com.unimerch.repository.native_query_dto.order.OrderNativeQueryDTORepo;
import com.unimerch.repository.order.OrderRepository;
import com.unimerch.repository.product.BrgProductTagTagGroupRepository;
import com.unimerch.repository.product.ProductRepository;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticServiceImpl implements AnalyticService {

    @Autowired
    private OrderNativeQueryDTORepo orderNativeQueryDTORepo;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrgProductTagTagGroupRepository brgProductTagTagGroupRepo;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MessageSource messageSource;

    public AmznFilter getAmznFilter(AnalyticsParam analyticsParam) {
        int groupId = analyticsParam.getGroupId();
        int amznId = analyticsParam.getAmznId();
        return groupId == 0 && amznId == 0 ? AmznFilter.ALL : groupId != 0 && amznId == 0 ? AmznFilter.GROUP : AmznFilter.AMZN;
    }

    @Override
    public OrderChartResult getChartAnalytics(AnalyticsParam analyticsParam) {
        DateFilter dateFilter = DateFilter.parseDateFilter(analyticsParam.getDateFilter());
        AmznFilter amznFilter = getAmznFilter(analyticsParam);

        switch (dateFilter) {
            case TODAY:
                return getChartToday(analyticsParam, amznFilter);
            case YESTERDAY:
                return getChartYesterday(analyticsParam, amznFilter);
            case THIS_MONTH:
                return getChartThisMonth(analyticsParam, amznFilter);
            case PREVIOUS_MONTH:
                return getChartPreviousMonth(analyticsParam, amznFilter);
            case CUSTOM:
                return getChartCustomDateRange(analyticsParam, amznFilter);
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public OrderChartResult getChartToday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantToday();
        Instant endDate = Instant.now();
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartYesterday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantYesterday = TimeUtils.getInstantYesterday();
        Instant startDate = instantYesterday.get("startTime");
        Instant endDate = instantYesterday.get("endTime");
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartThisMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantThisMonth();
        Instant endDate = Instant.now();
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartPreviousMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantPreviousMonth = TimeUtils.getInstantPreviousMonth();
        Instant startDate = instantPreviousMonth.get("startTime");
        Instant endDate = instantPreviousMonth.get("endTime");
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartCustomDateRange(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.convertStringToInstant(analyticsParam.getStartDate(), TimeUtils.dayMonthYearPattern);
        Instant endDate = TimeUtils.convertStringToInstant(analyticsParam.getEndDate(), TimeUtils.dayMonthYearPattern).plus(Period.ofDays(1));
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public List<OrderChartColumn> getOrderChartColumnList(AnalyticsParam analyticsParam, AmznFilter amznFilter, Instant startDate, Instant endDate) {
        switch (amznFilter) {
            case ALL:
                return orderNativeQueryDTORepo.findAllOrderChartWithDateRange(startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            case GROUP:
                return orderNativeQueryDTORepo.findGroupOrderChartWithDateRange(analyticsParam.getGroupId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            case AMZN:
                return orderNativeQueryDTORepo.findAmznOrderChartWithDateRange(analyticsParam.getAmznId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public OrderChartResult processOrderChartList(List<OrderChartColumn> orderChartColumnList) {
        List<String> dates = new LinkedList<>();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();

        orderChartColumnList.forEach(item -> {
            dates.add(TimeUtils.toDayMonthYearPattern(item.getDate()));
            royalties.add(item.getRoyalties());
            soldNumbers.add(item.getSold());
        });

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

    @Override
    public List<ProductAnalyticsResult> getProductAnalyticsList(AnalyticsParam analyticsParam) {
        DateFilter dateFilter = DateFilter.parseDateFilter(analyticsParam.getDateFilter());
        AmznFilter amznFilter = getAmznFilter(analyticsParam);

        switch (dateFilter) {
            case TODAY:
                return getProductAnalyticsToday(analyticsParam, amznFilter);
            case YESTERDAY:
                return getProductAnalyticsYesterday(analyticsParam, amznFilter);
            case THIS_MONTH:
                return getProductAnalyticsThisMonth(analyticsParam, amznFilter);
            case PREVIOUS_MONTH:
                return getProductAnalyticsPreviousMonth(analyticsParam, amznFilter);
            case CUSTOM:
                return getProductAnalyticsCustomDateRange(analyticsParam, amznFilter);
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public List<ProductAnalyticsResult> getProductAnalyticsToday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantToday();
        Instant endDate = Instant.now();
        List<ProductResult> productResultList = getProductResultList(analyticsParam, amznFilter, startDate, endDate);

        return processProductResultList(productResultList);
    }

    public List<ProductAnalyticsResult> getProductAnalyticsYesterday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantYesterday = TimeUtils.getInstantYesterday();
        Instant startDate = instantYesterday.get("startTime");
        Instant endDate = instantYesterday.get("endTime");
        List<ProductResult> productResultList = getProductResultList(analyticsParam, amznFilter, startDate, endDate);

        return processProductResultList(productResultList);
    }

    public List<ProductAnalyticsResult> getProductAnalyticsThisMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantThisMonth();
        Instant endDate = Instant.now();
        List<ProductResult> productResultList = getProductResultList(analyticsParam, amznFilter, startDate, endDate);

        return processProductResultList(productResultList);
    }

    public List<ProductAnalyticsResult> getProductAnalyticsPreviousMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantPreviousMonth = TimeUtils.getInstantPreviousMonth();
        Instant startDate = instantPreviousMonth.get("startTime");
        Instant endDate = instantPreviousMonth.get("endTime");
        List<ProductResult> productResultList = getProductResultList(analyticsParam, amznFilter, startDate, endDate);

        return processProductResultList(productResultList);
    }

    public List<ProductAnalyticsResult> getProductAnalyticsCustomDateRange(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.convertStringToInstant(analyticsParam.getStartDate(), TimeUtils.dayMonthYearPattern);
        Instant endDate = TimeUtils.convertStringToInstant(analyticsParam.getEndDate(), TimeUtils.dayMonthYearPattern).plus(Period.ofDays(1));
        List<ProductResult> productResultList = getProductResultList(analyticsParam, amznFilter, startDate, endDate);

        return processProductResultList(productResultList);
    }

    public List<ProductResult> getProductResultList(AnalyticsParam analyticsParam, AmznFilter amznFilter, Instant startDate, Instant endDate) {
        switch (amznFilter) {
            case ALL:
                return orderNativeQueryDTORepo.getAllProductAnalyticsList(startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size(),
                        analyticsParam.isSearchable(), analyticsParam.getSearchKey());
            case GROUP:
                return orderNativeQueryDTORepo.getGroupProductAnalyticsList(analyticsParam.getGroupId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size(),
                        analyticsParam.isSearchable(), analyticsParam.getSearchKey());
            case AMZN:
                return orderNativeQueryDTORepo.getAmznProductAnalyticsList(analyticsParam.getAmznId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size(),
                        analyticsParam.isSearchable(), analyticsParam.getSearchKey());
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public List<ProductAnalyticsResult> processProductResultList(List<ProductResult> productResultList) {
        return productResultList.stream()
                .sorted((o1, o2) -> Long.compare(o2.getQuantitySold(), o1.getQuantitySold()))
                .map(productResult -> {
                    List<TagGroupTagResult> tagGroupTagResultList = brgProductTagTagGroupRepo.findTagGroupAndTagByAsin(productResult.getAsin());
                    return productMapper.toProductAnalyticsResult(productResult, tagGroupTagResultList);
                }).collect(Collectors.toList());
    }

    @Override
    public OrderCardResult getCardAnalytics(AnalyticsParam analyticsParam) {
        DateFilter dateFilter = DateFilter.parseDateFilter(analyticsParam.getDateFilter());
        AmznFilter amznFilter = getAmznFilter(analyticsParam);

        switch (dateFilter) {
            case TODAY:
                return getCardToday(analyticsParam, amznFilter);
            case YESTERDAY:
                return getCardYesterday(analyticsParam, amznFilter);
            case THIS_MONTH:
                return getCardThisMonth(analyticsParam, amznFilter);
            case PREVIOUS_MONTH:
                return getCardPreviousMonth(analyticsParam, amznFilter);
            case CUSTOM:
                return getCardCustomDateRange(analyticsParam, amznFilter);
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    private OrderCardResult getCardToday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantToday();
        Instant endDate = Instant.now();

        return getOrderCardResult(analyticsParam, amznFilter, startDate, endDate);
    }

    private OrderCardResult getCardYesterday(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantYesterday = TimeUtils.getInstantYesterday();
        Instant startDate = instantYesterday.get("startTime");
        Instant endDate = instantYesterday.get("endTime");

        return getOrderCardResult(analyticsParam, amznFilter, startDate, endDate);
    }

    private OrderCardResult getCardThisMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.getInstantThisMonth();
        Instant endDate = Instant.now();

        return getOrderCardResult(analyticsParam, amznFilter, startDate, endDate);
    }

    private OrderCardResult getCardPreviousMonth(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Map<String, Instant> instantPreviousMonth = TimeUtils.getInstantPreviousMonth();
        Instant startDate = instantPreviousMonth.get("startTime");
        Instant endDate = instantPreviousMonth.get("endTime");

        return getOrderCardResult(analyticsParam, amznFilter, startDate, endDate);
    }

    private OrderCardResult getCardCustomDateRange(AnalyticsParam analyticsParam, AmznFilter amznFilter) {
        Instant startDate = TimeUtils.convertStringToInstant(analyticsParam.getStartDate(), TimeUtils.dayMonthYearPattern);
        Instant endDate = TimeUtils.convertStringToInstant(analyticsParam.getEndDate(), TimeUtils.dayMonthYearPattern).plus(Period.ofDays(1));

        return getOrderCardResult(analyticsParam, amznFilter, startDate, endDate);
    }

    public OrderCardResult getOrderCardResult(AnalyticsParam analyticsParam, AmznFilter amznFilter, Instant startDate, Instant endDate) {
        switch (amznFilter) {
            case ALL:
                return orderNativeQueryDTORepo.getAllOrderCartResult(startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            case GROUP:
                return orderNativeQueryDTORepo.getGroupOrderCartResult(analyticsParam.getGroupId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            case AMZN:
                return orderNativeQueryDTORepo.getAmznOrderCartResult(analyticsParam.getAmznId(), startDate, endDate,
                        analyticsParam.getTagIncluded(), analyticsParam.getTagIncluded().size());
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

}

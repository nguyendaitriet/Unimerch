class AnalyticsParam {
    constructor(groupId, amznId, dateFilter, startDate, endDate) {
        this.groupId = groupId;
        this.amznId = amznId;
        this.dateFilter = dateFilter;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

class DailyRevenue {
    constructor(date, royalties, soldNumbers) {
        this.date = date;
        this.royalties = royalties;
        this.soldNumbers = soldNumbers;
    }
}

class MonthlyOrderChartResult {
    constructor(monthList, royaltiesList, soldNumbersList) {
        this.monthList = monthList;
        this.royaltiesList = royaltiesList;
        this.soldNumbersList = soldNumbersList;
    }
}

class TagGroup {
    constructor(id, name, color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}

class ProductTagTagGroup {
    constructor(asin, tagGroupId, tagId) {
        this.asin = asin;
        this.tagGroupId = tagGroupId;
        this.tagId = tagId;
    }
}


class AnalyticsParam {
    constructor(groupId, amznId, dateFilter, startDate, endDate, tagIncluded, tagExcluded) {
        this.groupId = groupId;
        this.amznId = amznId;
        this.dateFilter = dateFilter;
        this.startDate = startDate;
        this.endDate = endDate;
        this.searchable = false;
        this.searchKey = '';
        this.tagIncluded = tagIncluded;
        this.tagExcluded = tagExcluded;
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
    constructor(tagGroupId, tagId) {
        this.tagGroupId = tagGroupId;
        this.tagId = tagId;
    }
}


let result = {
    "dates": [
        "05/01/2023"
    ],
    "royalties": [
        31.95
    ],
    "soldNumbers": [
        32
    ]
};

let dailyRevenueArray = [];
class DailyRevenue {
    constructor(date, royalties, soldNumbers) {
        this.date = date;
        this.royalties = royalties;
        this.soldNumbers = soldNumbers
    }
}

let dateCount = result.dates.length;
for (let i = 0; i < dateCount; i++) {
    let dailyRevenue = new DailyRevenue(result.dates[i], result.royalties[i], result.soldNumbers[i]);
    dailyRevenueArray.push(dailyRevenue);
}

function groupBy(list, keyGetter) {
    const map = new Map();
    list.forEach((item) => {
        const key = keyGetter(item);
        const collection = map.get(key);
        if (!collection) {
            map.set(key, [item]);
        } else {
            collection.push(item);
        }
    });
    return map;
}

let monthGroup = groupBy(dailyRevenueArray, dailyRevenue => dailyRevenue.date.substring(3));

let monthTitle = [];
let royaltiesSum = [];
let soldNumbersSum = [];

for (let [key, value] of monthGroup.entries()) {
    monthTitle.push(key);
    let royalties = 0;
    let soldNumbers = 0;
    value.forEach((item, index) => {
        royalties += item.royalties;
        soldNumbers += item.soldNumbers;
    })
    let roundedRoyalties = Math.round((royalties + Number.EPSILON) * 100) / 100;
    royaltiesSum.push(roundedRoyalties);
    soldNumbersSum.push(soldNumbers);
}

console.log(monthTitle);
console.log(royaltiesSum);
console.log(soldNumbersSum);




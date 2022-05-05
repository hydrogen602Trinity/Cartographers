import { computePageCount } from "utilities/paging";

it("paging", () => {
    expect(computePageCount(13, 10)).toEqual(2);
    expect(computePageCount(13, 3)).toEqual(5);
    expect(computePageCount(12, 3)).toEqual(4);
});

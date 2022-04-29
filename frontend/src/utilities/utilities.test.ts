import { getPublicPath } from "utilities/env";
import { computePageCount } from "utilities/paging";

export { };

it("paging", () => {
    expect(computePageCount(13, 10)).toEqual(2);
    expect(computePageCount(13, 3)).toEqual(5);
    expect(computePageCount(12, 3)).toEqual(4);
});

it("getPublicPath test undefined PUBLIC_URL", () => {
    delete process.env.PUBLIC_URL;
    expect(process.env.PUBLIC_URL).toBe(undefined);

    expect(getPublicPath("img/some_image.png")).toBe("/img/some_image.png");
    expect(getPublicPath("/idk.png")).toBe("/idk.png");
});

it("getPublicPath test defined PUBLIC_URL", () => {
    process.env.PUBLIC_URL = "somedomain.local";
    expect(process.env.PUBLIC_URL).toBe("somedomain.local");

    expect(getPublicPath("img/some_image.png")).toBe("somedomain.local/img/some_image.png");
    expect(getPublicPath("/idk.png")).toBe("somedomain.local/idk.png");
});

it("getPublicPath test defined PUBLIC_URL with /", () => {
    process.env.PUBLIC_URL = "somedomain.local/";
    expect(process.env.PUBLIC_URL).toBe("somedomain.local/");

    expect(getPublicPath("img/some_image.png")).toBe("somedomain.local/img/some_image.png");
    expect(getPublicPath("/idk.png")).toBe("somedomain.local/idk.png");
});



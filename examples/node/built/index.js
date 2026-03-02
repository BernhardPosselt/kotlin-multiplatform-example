"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const emporixapi_1 = require("emporixapi");
const kotlin_kotlin_stdlib_mjs_1 = require("emporixapi/kotlin-kotlin-stdlib.mjs");
const config = {
    endpoint: "https://webhook.site/84e3873b-4d08-4191-bd5a-ad7d7d07524a",
    tenant: "fyayc"
};
const api = new emporixapi_1.ProductApi(config);
const name = new Map();
name.set("en", "yo");
api.createProduct(new emporixapi_1.CreateBasicProduct("code", kotlin_kotlin_stdlib_mjs_1.KtMap.fromJsMap(name)), "token", false, false, null);
console.log((0, emporixapi_1.returnAMap)().asJsReadonlyMapView().get("test"));

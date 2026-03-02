import {ApiConfiguration, CreateBasicProduct, ProductApi, returnAMap} from "emporixapi";
import {KtMap} from "emporixapi/kotlin-kotlin-stdlib.mjs";

const config: ApiConfiguration = {
    endpoint: "https://webhook.site/84e3873b-4d08-4191-bd5a-ad7d7d07524a",
    tenant: "fyayc"
}
const api = new ProductApi(config);
const name = new Map<string, string>();
name.set("en", "yo");
api.createProduct(new CreateBasicProduct(
        "code",
        KtMap.fromJsMap(name),
    ),
    "token",
    false,
    false,
    null,
)

console.log(returnAMap().asJsReadonlyMapView().get("test"))
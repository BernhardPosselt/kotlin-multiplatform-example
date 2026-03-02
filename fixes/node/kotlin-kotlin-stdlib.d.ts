import {KtMap as M} from "emporixapi";

declare module "emporixapi/kotlin-kotlin-stdlib" {
    export namespace KtMap {
        export function fromJsMap<K, V>(map: ReadonlyMap<K, V>): M<K, V>;
    }
}

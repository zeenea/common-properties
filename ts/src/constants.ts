import { CommonProperty, Type } from './types';

export const catalogCommonProperty: CommonProperty = {
    uuid: "610b8e06-7fcb-4de6-b1a6-594b05901463",
    defaultName: "Catalog",
    defaultDescription: "Common Property Catalog",
    machineDescription: "Source system catalog storing the item. In some source systems, \"database\" is replaced by catalog.",
    type: Type.TAG,
    isPropagable: true
};

export const databaseCommonProperty: CommonProperty = {
    uuid: "2e3ac846-79b5-4953-8b76-a0cbe794453b",
    defaultName: "Database",
    defaultDescription: "Common Property Database",
    machineDescription: "Name or identifier of the Database storing the item",
    type: Type.TAG,
    isPropagable: true
};

export const schemaCommonProperty: CommonProperty = {
    uuid: "7055eb42-329e-11ee-a1c1-af9eec72241a",
    defaultName: "Schema",
    defaultDescription: "Common Property Schema",
    machineDescription: "Name or identifier of the Schema (Database schema, etc) storing the item",
    type: Type.TAG,
    isPropagable: true
};

export const typeCommonProperty: CommonProperty = {
    uuid: "cbfe45a3-986f-4b7b-bfc2-23a022a31012",
    defaultName: "Type",
    defaultDescription: "Common Property Type",
    machineDescription: "Item type from source system",
    type: Type.TAG,
    isPropagable: false
};

export const sqlQueryProperty: CommonProperty = {
    uuid: "18c61716-e110-40c1-8ca2-3a1468842a4a",
    defaultName: "SQL Query",
    defaultDescription: "SQL Queries executed by the process",
    machineDescription: "SQL query associated with a process or a dataset",
    type: Type.LONG_TEXT,
    isPropagable: false
};

export const sqlQueryDialectProperty: CommonProperty = {
    uuid: "b94ab627-9b06-4866-b304-47028baf5608",
    defaultName: "SQL Query dialect",
    defaultDescription: "Dialect of SQL Queries executed by the process",
    machineDescription: "Dialect (ANSI, T-SQL, etc) of an SQL query executed by a process or a view",
    type: Type.TAG,
    isPropagable: false
};

export const accessUrlProperty: CommonProperty = {
    uuid: "08bfb9ee-b161-11ee-9cf6-1f4507e5336f",
    defaultName: "Access URL",
    defaultDescription: "Common Property Access URL",
    machineDescription: "URL to access the item in the source system",
    type: Type.URL,
    isPropagable: false
};

export const expressionProperty: CommonProperty = {
    uuid: "2188c4bc-fdc5-43a6-a895-89afd9d28f81",
    defaultName: "Expression",
    defaultDescription: "Field expression",
    machineDescription: "Calculated field expression description",
    type: Type.LONG_TEXT,
    isPropagable: false
};

export const powerQueryProperty: CommonProperty = {
    uuid: "8691460c-a2b8-4f59-970f-fd3f026a6071",
    defaultName: "Power Query",
    defaultDescription: "Power Query",
    machineDescription: "PowerQuery associated with PowerBI datasets from semantic model",
    type: Type.LONG_TEXT,
    isPropagable: false
};

export const dataSourceTypeProperty: CommonProperty = {
    uuid: "68d7f77d-06c2-4772-91cc-877f7eb8bc08",
    defaultName: "Datasource Type",
    defaultDescription: "Common Property Datasource Type",
    machineDescription: "Entity type in the data source",
    type: Type.TAG,
    isPropagable: false
};

export const commonProperties: ReadonlySet<CommonProperty> = new Set([
    accessUrlProperty,
    catalogCommonProperty,
    databaseCommonProperty,
    schemaCommonProperty,
    typeCommonProperty,
    sqlQueryProperty,
    sqlQueryDialectProperty,
    expressionProperty,
    powerQueryProperty,
    dataSourceTypeProperty
]);

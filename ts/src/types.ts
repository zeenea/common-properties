export enum Type {
    TAG = "TAG",
    LONG_TEXT = "LONG_TEXT",
    URL = "URL"
}

export interface CommonProperty {
    uuid: string;
    defaultName: string;
    defaultDescription: string;
    machineDescription: string;
    type: Type;
    isPropagable: boolean;
}

export interface DataSourceIdentifierKey {
    key: string;
}

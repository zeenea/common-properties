const fs = require("fs");
const path = require("path");
const yaml = require("js-yaml");

const yamlPath = path.resolve(
  __dirname,
  "../../lib/src/main/resources/common-properties.yml"
);
const outputPath = path.resolve(__dirname, "../src/constants.ts");

const doc = yaml.load(fs.readFileSync(yamlPath, "utf8"));

function generateProperty(p) {
  return `export const ${p.attributeName}: CommonProperty = {
    uuid: "${p.uuid}",
    defaultName: "${p.defaultName}",
    defaultDescription: "${p.defaultDescription}",
    machineDescription: "${p.machineDescription}",
    type: Type.${p.type},
    isPropagable: ${p.isPropagable}
};`;
}

const properties = doc.commonProperties;

const sortedNames = properties
  .map((p) => p.attributeName)
  .sort();

const lines = [
  `import { CommonProperty, Type } from './types';`,
  "",
  ...properties.map((p) => generateProperty(p)),
  "",
  `export const commonProperties: ReadonlySet<CommonProperty> = new Set([`,
  ...sortedNames.map(
    (name, i) => `    ${name}${i < sortedNames.length - 1 ? "," : ""}`
  ),
  `]);`,
  "",
];

fs.writeFileSync(outputPath, lines.join("\n"));
console.log(`Generated ${outputPath} with ${properties.length} properties.`);
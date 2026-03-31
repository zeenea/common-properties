package zeenea.common.properties.datasource.reference;

import java.util.List;

public class SourceReference {

  private List<SourceReferenceItem> datasets;
  private List<SourceReferenceItem> visualizations;
  private List<SourceReferenceItem> dataProcesses;

  private SourceReferenceIdentification datasource;

  private List<String> connectorIds;

  public SourceReference() {}

  public SourceReferenceIdentification getDatasource() {
    return datasource;
  }

  public void setDatasource(SourceReferenceIdentification datasource) {
    this.datasource = datasource;
  }

  public List<String> getConnectorIds() {
    return connectorIds;
  }

  public void setConnectorIds(List<String> connectorIds) {
    this.connectorIds = connectorIds;
  }

  public List<SourceReferenceItem> getDatasets() {
    return datasets;
  }

  public void setDatasets(List<SourceReferenceItem> datasets) {
    this.datasets = datasets;
  }

  public List<SourceReferenceItem> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(List<SourceReferenceItem> visualizations) {
    this.visualizations = visualizations;
  }

  public List<SourceReferenceItem> getDataProcesses() {
    return dataProcesses;
  }

  public void setDataProcesses(List<SourceReferenceItem> dataProcesses) {
    this.dataProcesses = dataProcesses;
  }
}

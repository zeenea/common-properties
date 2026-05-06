import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;

public class GenerateDataSourceTypePlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    Provider<Directory> outputDir =
        project.getLayout().getBuildDirectory().dir("generated/datasource-type");

    TaskProvider<GenerateDataSourceTypeTask> task =
        project
            .getTasks()
            .register(
                "generateDataSourceType",
                GenerateDataSourceTypeTask.class,
                t -> {
                  t.getResourcesDir()
                      .set(
                          project
                              .getLayout()
                              .getProjectDirectory()
                              .dir("src/main/resources/datasources"));
                  t.getOutputDir().set(outputDir);
                });

    project
        .getExtensions()
        .getByType(JavaPluginExtension.class)
        .getSourceSets()
        .getByName("main")
        .getJava()
        .srcDir(task.map(t -> t.getOutputDir().get()));

    project.getTasks().named("compileJava", t -> t.dependsOn(task));
  }
}

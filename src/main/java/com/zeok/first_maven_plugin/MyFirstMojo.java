package com.zeok.first_maven_plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "summarize", defaultPhase = LifecyclePhase.INSTALL)

public class MyFirstMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${project.build.directory}")
	private File outputDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {

		File f = outputDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		File outputFile = new File(f, "outputFile.txt");

		FileWriter w = null;

		String groupId = project.getGroupId();
		String artifactId = project.getArtifactId();
		String version = project.getVersion();
		Object releaseDate = project.getProperties().get("release.date");
		List<Developer> developers = project.getDevelopers();
		List<Dependency> dependencies = project.getDependencies();
		List<Plugin> plugins = project.getBuildPlugins();
		try {
			w = new FileWriter(outputFile);
			w.write("Project info: " + groupId + "." + artifactId + "." + version);
			w.write(" Developers: ");
			for (Developer dev : developers) {
				w.write("Developer Name: " + dev.getName());
			}
			w.write(" Release Date: " + releaseDate);
			w.write(" Dependencies: ");
			for (Dependency dep : dependencies) {
				w.write("Dependency: " + dep.getGroupId() + "." + dep.getArtifactId());
			}
			w.write(" Plugins: ");
			for (Plugin p : plugins) {
				w.write("Plugin: " + p.getArtifactId() + " ");
			}
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + outputFile, e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

	}

}
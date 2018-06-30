package net.jackadull.jackadocs;

import net.jackadull.jackadocs.execution.JackadocsProjectInfo;

public final class JackadocsInfo {
  public static final String ArtifactID = "${project.artifactId}";
  public static final String GroupID = "${project.groupId}";
  public static final String Version = "${project.version}";

  public static final JackadocsProjectInfo ProjectInfo = JackadocsProjectInfo.apply(GroupID, ArtifactID, Version);

  private JackadocsInfo() {
    throw new UnsupportedOperationException();
  }
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.kyori.indra.git.IndraGitExtension
import org.eclipse.jgit.lib.Repository
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

fun Project.lastCommitHash(): String = the<IndraGitExtension>().commit()?.name?.substring(0, 7)
  ?: error("Could not determine commit hash")

fun Project.decorateVersion() {
  val versionString = version as String
  version = if (versionString.endsWith("-SNAPSHOT")) {
    "$versionString+${lastCommitHash()}"
  } else {
    versionString
  }
}

fun ShadowJar.reloc(pkg: String) {
  relocate(pkg, "squaremap.libraries.$pkg")
}

fun Project.currentBranch(): String {
  System.getenv("GITHUB_HEAD_REF")?.takeIf { it.isNotEmpty() }
    ?.let { return it }
  System.getenv("GITHUB_REF")?.takeIf { it.isNotEmpty() }
    ?.let { return it.replaceFirst("refs/heads/", "") }

  val indraGit = the<IndraGitExtension>().takeIf { it.isPresent }

  val ref = indraGit?.git()?.repository?.exactRef("HEAD")?.target
    ?: return "detached-head"

  return Repository.shortenRefName(ref.name)
}

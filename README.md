# Multi-Module Staging Workflow

[![Eclipse License](https://img.shields.io/badge/license-EPL--2.0-blue.svg)](https://www.eclipse.org/legal/epl-v20.html)

Demonstration project showcasing the build and release workflow for a multi-module Maven project using the [**Central Staging Plugins**](https://github.com/eclipse-cbi/central-staging-plugins) to publish artifacts to repo.eclipse.org and Maven Central.

## Overview

This project demonstrates a complete two-stage publication workflow:

1. **Publication to repo3.eclipse.org** : Publicly accessible artifacts for validation
2. **Publication to Maven Central** : Final release for the community

## Central Staging Plugins

This project uses the [**central-staging-plugins**](https://github.com/eclipse-cbi/central-staging-plugins) plugin developed by Eclipse Common Build Infrastructure (CBI). This Maven plugin facilitates publishing artifacts to Maven Central by automatically handling:

- Creation of bundles compliant with Maven Central requirements
- Generation of checksums (SHA-256, SHA-512)
- GPG signing of artifacts
- Upload to the Maven Central portal

**Official Documentation**: [https://github.com/eclipse-cbi/central-staging-plugins](https://github.com/eclipse-cbi/central-staging-plugins)


## Publication Workflow

### 1. Continuous Build and Publication (Main Branch)

On every push to the `main` branch, the GitHub Actions workflow:

- ✅ Automatically removes the `-SNAPSHOT` suffix from the version
- ✅ Builds the project with `mvn clean deploy`
- ✅ Publishes artifacts to **repo3.eclipse.org** (staging repository)
- ✅ Artifacts are **publicly accessible** for validation

**Repository URL**: `https://repo3.eclipse.org/repository/cbi-maven2-staging/`

### 2. Snapshot Build (Other Branches)

On branches other than `main`:

- ✅ Ensures the version contains the `-SNAPSHOT` suffix
- ✅ Publishes to the repo3.eclipse.org snapshots repository

**Snapshots repository URL**: `https://repo3.eclipse.org/repository/cbi-maven2-snapshots/`

### 3. Release to Maven Central (Tags)

When creating a tag (format `v*.*.*`, e.g., `v1.0.0`):

- ✅ Activates the Maven `central-release` profile
- ✅ Signs artifacts with GPG
- ✅ Creates a bundle for Maven Central using the `central-staging-plugins` plugin
- ✅ Automatic upload to Maven Central via the `rc-upload` goal
- ✅ Creates a GitHub Release

**Commands to create a release**:
```bash
git tag v1.0.0
git push origin v1.0.0
```

## Maven Configuration

### Distribution Management

Repositories are configured with variables in the `pom.xml`:

```xml
<properties>
    <eclipse.repo.id>repo3.eclipse.org</eclipse.repo.id>
    <eclipse.repo.url>https://repo3.eclipse.org</eclipse.repo.url>
    <eclipse.staging.path>/repository/cbi-maven2-staging/</eclipse.staging.path>
    <eclipse.snapshots.path>/repository/cbi-maven2-snapshots/</eclipse.snapshots.path>
</properties>

<distributionManagement>
    <repository>
        <id>${eclipse.repo.id}</id>
        <name>CBI Staging Repository</name>
        <url>${eclipse.repo.url}${eclipse.staging.path}</url>
    </repository>
    <snapshotRepository>
        <id>${eclipse.repo.id}</id>
        <name>CBI Snapshots Repository</name>
        <url>${eclipse.repo.url}${eclipse.snapshots.path}</url>
    </snapshotRepository>
</distributionManagement>
```

### Central Release Profile

The `central-release` profile configures the plugin for Maven Central:

```xml
<profile>
    <id>central-release</id>
    <build>
        <plugins>
            <!-- GPG Signing -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-gpg-plugin</artifactId>
                <executions>
                    <execution>
                        <id>sign-artifacts</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>sign</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- Central Staging Plugin -->
            <plugin>
                <groupId>org.eclipse.cbi.central</groupId>
                <artifactId>central-staging-plugins</artifactId>
                <executions>
                    <!-- Bundle creation -->
                    <execution>
                        <id>create-bundle</id>
                        <phase>package</phase>
                        <goals>
                            <goal>rc-bundle</goal>
                        </goals>
                        <configuration>
                            <repositoryUrl>${eclipse.repo.url}${eclipse.staging.path}</repositoryUrl>
                            <signArtifacts>true</signArtifacts>
                            <generateChecksums256>true</generateChecksums256>
                            <generateChecksums512>true</generateChecksums512>
                        </configuration>
                    </execution>
                    
                    <!-- Upload to Maven Central -->
                    <execution>
                        <id>upload-to-central</id>
                        <phase>deploy</phase>
                        <goals>
                            <goal>rc-upload</goal>
                        </goals>
                        <configuration>
                            <automaticPublishing>false</automaticPublishing>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- Skip default deploy -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Required GitHub Actions Secrets

For the workflow to function, configure the following secrets in your GitHub repository:

| Secret | Description |
|--------|-------------|
| `REPO3_TOKEN_USERNAME` | Username for repo3.eclipse.org |
| `REPO3_TOKEN_PASSWORD` | Token/Password for repo3.eclipse.org |
| `GPG_PRIVATE_KEY` | GPG private key (ASCII-armored format) |
| `GPG_PASSPHRASE` | GPG key passphrase |

## Local Usage

### Standard Build

```bash
mvn clean install
```

### Deploy to repo3.eclipse.org

`settings.xml` is needed

```bash
mvn clean deploy
```

### Release to Maven Central

`settings.xml` is needed

```bash
mvn clean deploy -Pcentral-release
```

## GitHub Actions Workflow

The `.github/workflows/release.yml` file orchestrates the entire process:

- **Trigger on `main` push**: Build and publication to repo3.eclipse.org (Staging repository)
- **Trigger on `v*.*.*` tag**: Complete release to Maven Central
- **Trigger on other branches**: Snapshot build and publication (Snapshot repository)

## Useful Links

- **Central Staging Plugins** : [https://github.com/eclipse-cbi/central-staging-plugins](https://github.com/eclipse-cbi/central-staging-plugins)
- **Repo3 Eclipse.org** : [https://repo3.eclipse.org/](https://repo3.eclipse.org/)
- **Maven Central Portal** : [https://central.sonatype.com/](https://central.sonatype.com/)

## License

This project is licensed under the [Eclipse Public License 2.0](https://www.eclipse.org/legal/epl-v20.html).

## Contributing

Contributions are welcome! Feel free to open an issue or a pull request.

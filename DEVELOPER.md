# Contributing to simple-http
                   
##Development

simple-http welcomes collaboration. Please use the existing code base as an advert for how we like our code and please ensure you keep the Maven build passing - `mvn clean test`.

##Releasing

We release to the Sonatype Central Repository based on [this guide](https://central.sonatype.org/pages/ossrh-guide.html).

To perform a release you will need to create a PGP signature - follow [these instructions](https://central.sonatype.org/pages/working-with-pgp-signatures.html).

After this you will need to configure your Maven settings.xml to contain the following:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id>
            <username>$your-jira-id$</username>
            <password>$your-jira-pwd$</password>
        </server>
    </servers>

    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.executable>gpg2</gpg.executable>
                <gpg.passphrase>$your_pass_phrase$</gpg.passphrase>
            </properties>
        </profile>
    </profiles>
</settings>
```

A deployment of a snapshot can be performed with `mvn clean deploy`.

A stable release should be performed with `mvn release:clean release:prepare release:perform`.

You can get a public key via `gpg --keyserver hkp://pool.sks-keyservers.net --recv-keys 615BBAE78C770510E73A7650BB180A5906DF61F1`.
`
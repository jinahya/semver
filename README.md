# semver
[![Dependency Status](https://www.versioneye.com/user/projects/5669f12843cfea00310001c6/badge.svg)](https://www.versioneye.com/user/projects/5669f12843cfea00310001c6)
[![Build Status](https://travis-ci.org/jinahya/semver.svg)](https://travis-ci.org/jinahya/semver)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/semver.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22semver%22)
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

A small library implementing [Semantic Versioning](http://semver.org).
## Versions

## Usages
### Build metadata
```java
final BuildMetadata.Builder builder
    = new BuildMetadata.Builder().identifiers("exp", "sha", "5114f85");
final BuildMetadata built = builder.build();
assertEquals(built.toString(), "exp.sha.5114f85");
```
### Pre-release version
```java
final PreReleaseVersion.Builder builder
    = new PreReleaseVersion.Builder().identifiers("x", "7", "z", "92");
final PreReleaseVersion built = builder.build();
assertEquals(built.toString(), "x.7.z.92");
```
### Normal version
```java
final NormalVersion.Builder builder = new NormalVersion.Builder()
    .majorVersion(0)
    .minorVersion(1)
    .patchVersion(0)
    .preReleaseVersion(
        new PreReleaseVersion.Builder()
        .identifiers("x", "7", "z", "92"))
    .buildMetadata(
        new BuildMetadata.Builder()
        .identifiers("exp", "sha", "5114f85"));
final NormalVersion built = builder.build();
assertEquals(built.toString(), "0.1.0-x.7.z.92+exp.sha.5114f85");
```
> Patch and minor version MUST be reset to 0 when major version is incremented.

```java
final NormalVersion.Builder builder = new NormalVersion.Builder()
    .majorVersion(0)
    .minorVersion(1)
    .patchVersion(2);
final NormalVersion built = builder.build();
assertEquals(built.toString(), "0.1.2");
assertEquals(built.getMajorVersionIncreased().toString(), "1.0.0");
```
> Patch version MUST be reset to 0 when minor version is incremented.

```java
final NormalVersion.Builder builder = new NormalVersion.Builder()
    .majorVersion(1)
    .minorVersion(0)
    .patchVersion(1);
final NormalVersion built = builder.build();
assertEquals(built.toString(), "1.0.1");
assertEquals(built.getMinorVersionIncreased().toString(), "1.2.0");
```
<hr/>
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

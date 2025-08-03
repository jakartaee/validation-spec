# Guide for maintainers of Jakarta Validation Specification

This guide is intended for maintainers of Jakarta Validation,
i.e. anybody with direct push access to the git repository.

## Preparing for the next iteration

When preparing the repository for the next major/minor version:

* Update the project version (if not already done by the release) using the following maven command
    ```
    mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$NEW_VERSION
    ```
* Update the versions in the following files: 
  * [xml-descriptor.asciidoc](sources/xml-descriptor.asciidoc)
  * [tck-audit.xsl](tck-audit.xsl)
* Create a placeholder section for "What's new" in [whatsnew.asciidoc](sources/whatsnew.asciidoc)
* Generate a new `tck-audit.xml` (see [README.md](README.md)) and push it to the [Jakarta Validation TCK](https://github.com/jakartaee/validation-tck) repository.

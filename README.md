# Bean Validation specification

This repository contains the Bean Validation specification. 
For more information on Bean Validation and the work in progress,
go to <http://beanvalidation.org>.

## Building docs

You need to have Apache Ant installed on your system and available on your classpath. The build
file _build.xml_ is located in this directory and all commands are relative to this directory.

* Running `ant all.doc` builds both PDF and HTML output in the _build/_ directory. `all.doc` is
also the default target.
* Running `ant clean` will clean up output HTML and PDF files.

## Tagging phrases for the TCK

The [Bean Validation TCK](https://github.com/beanvalidation/beanvalidation-tck) is a suite of unit
tests for validating the compliance of Bean Validation implementations with the specification.

The tests of the TCK are based on assertions representing sentences and phrases in this
specification. The `role` attribute is used to mark those text elements (`<para>`, `phrase` etc.)
of the specification which shall lead to an assertion in the TCK. The following values are allowed:

* `tck-testable`: The tagged element shall be represented by a testable assertion in the TCK
* `tck-not-testable`: The tagged element shall be represented by a non-testable assertion in the
TCK (e.g. assertions regarding thread safety)
* `tck-ignore`: The tagged element shall be excluded when creating a TCK assertion for an outer
element. Can be used to exlude explanatory phrases contained in a `para` marked as `tck-testable`.
* `tck-needs-update`: The tagged element shall be marked with a note in the TCK audit file saying
that the tests for this assertion need to be updated, e.g. due to a spec update. Can be used
together with `tck-testable` and `tck-not-testable`: `<para role="tck-testable tck-needs-update">`.

### Updating the TCK audit file

The TCK audit file is an XML file containing all assertions of the TCK. This file is generated with
help of an XSL transformation which is applied to the DocBook files.

The generation is executed by running `ant createTckAuditFile`. This is required whenever tagged
elements have been added, updated, removed or changed their position within all tagged elements of
a section.

The generated file _build/tck-audit.xml_ should be formatted with a line width of 100 characters
(allowing for easier comparisons between versions) and must be checked into the
[TCK project](https://github.com/beanvalidation/beanvalidation-tck/blob/master/tests/src/main/resources/tck-audit.xml).
If an update changed the section numbers of existing assertions, the corresponding tests need to be
adapted as well since they reference the section numbers in the `@SpecAssertion` annotation.

## Contributing to the specification

The recommended approach to contribute to the spec is via GitHub pull requests. 
More on contribution at <http://beanvalidation.org/contribute/>

Recommended tools by decreasing order of preference:

1. [XMLMind XML Editor](http://www.xmlmind.com/xmleditor/)
2. Any XML editor

Make sure to not go beyond 80 columns per line.

For the API, in particular the JavaDoc, follow the conventions described at
<https://community.jboss.org/wiki/ContributingToHibernateValidator#Coding_Guidelines>.

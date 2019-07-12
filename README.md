# Jakarta Bean Validation specification

This repository contains the Jakarta Bean Validation specification. 
For more information on Jakarta Bean Validation and the work in progress,
go to <http://beanvalidation.org>.

## Building docs

The specification is written in the AsciiDoc format. In order to convert this into nicely rendered
output, you need to have Apache Ant installed on your system and available on your classpath. The build
file _build.xml_ is located in this directory and all commands are relative to this directory.

* Running `ant all.doc` builds both PDF and HTML output in the _target/_ directory. `all.doc` is
also the default target.
* Running `ant clean` will clean up output HTML and PDF files.
* Running `ant render-html` will only build the HTML output (much faster).

## Tagging phrases for the TCK

The [Jakarta Bean Validation TCK](https://github.com/beanvalidation/beanvalidation-tck) is a suite of unit
tests for validating the compliance of Jakarta Bean Validation implementations with the specification.

The tests of the TCK are based on assertions representing sentences and phrases in this
specification. Labels on specific text elements of the specification are used to mark those which
should lead to an assertion in the TCK. The following values are allowed:

* `tck-testable`: The tagged element must be represented by a testable assertion in the TCK
* `tck-not-testable`: The tagged element must be represented by a non-testable assertion in the
TCK (e.g. assertions regarding thread safety)
* `tck-ignore`: The tagged element must be excluded when creating a TCK assertion for an outer
element. Can be used to exlude explanatory phrases contained in an element marked as `tck-testable`.
* `tck-needs-update`: The tagged element must be marked with a note in the TCK audit file saying
that the tests for this assertion need to be updated, e.g. due to a spec update. Can be used
together with `tck-testable` and `tck-not-testable`: `[tck-testable tck-needs-update]#Some sentence...#`.

### Updating the TCK audit file

The TCK audit file is an XML file containing all assertions of the TCK. This file is generated with
help of an XSL transformation (for that purpose the spec is converted into DocBook).

The generation is executed by running `ant create-tck-audit-file`. This is required whenever tagged
elements have been added, updated, removed or changed their position within all tagged elements of
a section.

The generated file _target/tck-audit.xml_ should not be re-formatted and must be checked into the
[TCK project](https://github.com/beanvalidation/beanvalidation-tck/blob/master/tests/src/main/resources/tck-audit.xml).
If an update changed the section numbers of existing assertions, the corresponding tests need to be
adapted as well since they reference the section numbers in the `@SpecAssertion` annotation.

## Contributing to the specification

By submitting a "pull request" or otherwise contributing to this repository, you agree to license your
contribution under the [Apache Software License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

The recommended approach to contribute to the spec is via GitHub pull requests. 
More on contribution at <http://beanvalidation.org/contribute/>

Make sure to not go beyond 80 columns per line.

### Style rules

- use `[...]` for omissions, prefer it on a dedicated line rather than inlined. For declaration, use a ; after `User user = [...];`
- remove email in `@author`
- use `@author` on classes or interfaces (non inner)
- use `{@link}` for method reference (the first in the JavaDoc of the element) except if it references external classes and if it references the class or element at bay.
- reference methods with a trailing `()` but be careful that some `methodname` are actually annotation attributes and thus should not have `()`
- in the spec use `` [methodname]`Foo.bar` `` and in the JavaDoc `{@link Foo#bar()}`
- no import statement
- add package statement
- use `<p/>` to separate paragraphs but do not use `<p>blah blah</p>`
- empty line between the `@param`, `@return`, `@throws` group and the other `@tag` elements
- `@throws` does not use `{@link }` for the exception
- import classes used by `{@link}` and `@throws` exceptions so that the unqualified name works
- something is true but a method returns `<literal>true</literal>`
- use `<ul>` for lists
- `<li>` are indented by 4 spaces compared to `<ul>`
- multiline `@param`, `@return` etc are indented to align with the beginning of the text of the element
- the core of the JavaDoc uses capital letters and punctuation like in plain English
- @tag attributes do not use capital letters for the beninning of a sentence nor use dots in the end, prefer ; to separate sentences in this situation
- use verbs at the third person instead of the imperative tense (?!): `Returns foo` instead of `Return foo`.
- use `<pre> </pre>` for multiline code example. `<pre>` elements should be on a separate line
- use an empty line between a class / interface declaration and the first element in the block

Here is an example

    /**
     * Returns the complementary elements related to {@link FooBar#baz()}.
     * This allows to break things apart.
     * <p/>
     * Demolition mileage may vary depending on the robustness
     * of {@code FooBar} when using {@code List}:
     * <ul>
     *     <li>This is a beginning</li>
     *     <li>This is the end</li>
     * </ul>
     * <pre>
     * Some code = new Some();
     * if (size >12) {
     *     code.doStuff();
     * }
     * </pre>
     *
     * @param test this is a long description that needs
     *        to be done on several lines, yep
     * @param foo this is another param
     * @return the result
     * @throws IllegalArgumentException when things happen
     *
     * @since 1.1
     * @deprecated
     */
    public void someMethod() {
        User user = [...];
        [...]
    }

    /**
     * Welcome to this element related to {@link FooBar}.
     * This allowed to break things apart.
     * <p/>
     * Demolition mileage may vary depending on the robustness
     * of {@code FooBar} when using {@code List}
     *
     * @author Emmanuel Bernard
     * @since 1.1
     * @deprecated
     */
    public class GoodStuff {

        /**
         * [...]
         */
        [...]
    }


For the API, in particular the JavaDoc, follow the conventions described at
<http://hibernate.org/validator/contribute/#coding-guidelines>.

## Showing tck assertion coverage

You can update the file `docinfo/index-docinfo.html` to show what is covered and what is not covered by
the TCK assertion coverage.

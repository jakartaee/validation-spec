<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ JBoss, Home of Professional Open Source
  ~ Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual contributors
  ~ by the @authors tag. See the copyright.txt in the distribution for a
  ~ full listing of individual contributors.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

<!--
  ~ Creates the TCK audit file based on the DocBook XML mark-up of the specification.
  ~
  ~ The generation of the audit file is controlled by marking individual sentences from the
  ~ specification as relevant for the TCK using the "role" attribute, which can be used in all
  ~ DocBook elements (paras, phrases etc.). The following values for the "role" attribute are
  ~ supported:
  ~
  ~ * tckTestable: Adds a testable assertion with the marked statement to the audit file
  ~ * tckNotTestable: Adds a not testable assertion with the marked statement to the audit file
  ~ * tckIgnore: Ignores the marked statement when given with another marked statement (e.g. to
  ~   exclude explanatory phrases)
  ~ * tckNeedsUpdate: Adds the note "Needs update" to the concerned statement (can be given
  ~   together with tck(Not)Testable)
  ~
  ~ Implementation note:
  ~
  ~ The generation happens in several passes, each processing a result tree fragment (RTF) created
  ~ by the previous pass:
  ~
  ~ * Merge all chapters referenced in the master file
  ~ * Determine the index numbers of all chapters and sections
  ~ * Expand xref elements into the index number of the referenced chapter or section
  ~ * Create the audit file
  ~
  ~ @author Gunnar Morling
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:exslt="http://exslt.org/common" xmlns:xi="http://www.w3.org/2001/XInclude" version="1.0">

    <xsl:output method="xml" indent="yes" xalan:indent-amount="4" />

    <xsl:param name="currentDate"/>

    <!-- ### Passes by creating and processing result tree fragments ### -->
    <xsl:variable name="merged">
        <xsl:apply-templates mode="merge" select="/"/>
    </xsl:variable>

    <xsl:variable name="withSectionNums">
        <xsl:apply-templates mode="addSectionNums" select="exslt:node-set($merged)"/>
    </xsl:variable>

    <xsl:variable name="prepared">
        <xsl:apply-templates mode="prepare" select="exslt:node-set($withSectionNums)"/>
    </xsl:variable>

    <xsl:template match="/">
        <xsl:apply-templates mode="createAuditFile" select="exslt:node-set($prepared)"/>
    </xsl:template>

    <!-- ### Merge templates ### -->

    <xsl:template match="xi:include" mode="merge">
        <xsl:variable name="fileName">en/<xsl:value-of select="@href" /></xsl:variable>
        <xsl:apply-templates select="document($fileName)" mode="merge"/>
    </xsl:template>

    <xsl:template match="@*|node()" mode="merge">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="merge"/>
        </xsl:copy>
    </xsl:template>

    <!-- ### addSectionNums templates ### -->

    <xsl:template match="chapter" mode="addSectionNums">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="sectionNum"><xsl:number from="book" level="single" /></xsl:attribute>
            <xsl:apply-templates mode="addSectionNums"/>
        </xsl:copy>
    </xsl:template>

       <xsl:template match="section" mode="addSectionNums">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="sectionNum"><xsl:number count="chapter | section" from="book" level="multiple" /></xsl:attribute>
            <xsl:apply-templates mode="addSectionNums"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*|node()" mode="addSectionNums">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="addSectionNums"/>
        </xsl:copy>
    </xsl:template>

    <!-- ### Prepare templates ### -->

    <xsl:template match="xref" mode="prepare">
        <xsl:variable name="id" select="@linkend"/>
        <xsl:value-of select="ancestor::*//*[@id=$id]/@sectionNum" />
    </xsl:template>

    <xsl:template match="*[@role = 'tckIgnore']" mode="prepare"/>

    <xsl:template match="@*|node()" mode="prepare">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="prepare"/>
        </xsl:copy>
    </xsl:template>

    <!-- ### Create audit file templates ### -->

    <xsl:template match="book"  mode="createAuditFile">

        <xsl:text>&#10;</xsl:text>
        <xsl:comment>
            <xsl:text> Generated by tck-audit.xsl at <xsl:value-of select="concat($currentDate, ' ')"/></xsl:text>
        </xsl:comment>
        <xsl:text>&#10;</xsl:text>

        <specification xmlns="http://jboss.com/products/weld/tck/audit" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://jboss.com/products/weld/tck/audit" name="JSR-349: Bean Validation"
            version="1.1.0" id="beanvalidation">

            <xsl:apply-templates mode="createAuditFile"/>

        </specification>
    </xsl:template>

    <xsl:template match="chapter" mode="createAuditFile">
        <section>
            <xsl:attribute name="id"><xsl:number from="book" level="single" /></xsl:attribute>
            <xsl:attribute name="title"><xsl:value-of select="normalize-space(title)" /></xsl:attribute>

            <!-- get all assertions directly under chapter, without a section -->
            <xsl:apply-templates select="*[not(local-name() = 'section')]" mode="createAuditFile"/>
        </section>

        <!-- get all sections, flattened to one level -->
        <xsl:apply-templates select=".//section" mode="createAuditFile"/>

    </xsl:template>

    <xsl:template match="section" mode="createAuditFile">

        <!-- add a section only if it has TCK-relevant sub-elements -->
        <xsl:if test=".//*[starts-with(@role, 'tck')]">
            <section>
                <xsl:attribute name="id"><xsl:number count="chapter | section" from="book" level="multiple" /></xsl:attribute>
                <xsl:attribute name="title"><xsl:value-of select="normalize-space(title)" /></xsl:attribute>

                <xsl:apply-templates select="*[not(local-name() = 'section')]" mode="createAuditFile"/>
            </section>
        </xsl:if>
    </xsl:template>

    <!-- Add an assertion for any element with role="tck..." -->
    <xsl:template match="*[starts-with(@role, 'tck')]" mode="createAuditFile">
        <xsl:variable name="normalized"><xsl:value-of select="normalize-space(.)" /></xsl:variable>
        <xsl:variable name="firstWord"><xsl:value-of select="substring-before(concat($normalized, ' '), ' ')"/></xsl:variable>

        <xsl:variable name="assertionText">
            <!-- Capitalize the first word if it doesn't contain upper-case letter already (e.g. a camel-cased method name) -->
            <xsl:choose>
                <xsl:when test="string-length(translate($firstWord, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ-', '')) &lt; string-length($firstWord)">
                    <xsl:value-of select="$normalized"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(translate(substring($normalized, 1, 1), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), substring($normalized, 2))"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <assertion>
            <xsl:attribute name="id">
                <xsl:number count="*[starts-with(@role, 'tck')]" from="section | chapter" level="any" format="a" />
            </xsl:attribute>
            <xsl:if test="contains(@role, 'tckNotTestable')">
                <xsl:attribute name="testable">false</xsl:attribute>
            </xsl:if>

            <text>
                <xsl:choose>
                    <!-- Remove trailing ":" -->
                    <xsl:when test="substring($assertionText, string-length($assertionText)) = ':'">
                        <xsl:value-of select="substring($assertionText, 0, string-length($assertionText))" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$assertionText" />
                    </xsl:otherwise>
                </xsl:choose>
            </text>

            <xsl:if test="contains(@role, 'tckNeedsUpdate')">
                <note>Needs update</note>
            </xsl:if>
        </assertion>
    </xsl:template>

    <xsl:template match="@*|node()" mode="createAuditFile">
        <xsl:apply-templates select="@*|node()" mode="createAuditFile"/>
    </xsl:template>

</xsl:stylesheet>

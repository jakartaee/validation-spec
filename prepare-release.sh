!#! /bin/bash

RELEASE_VERSION=$1
NEW_VERSION=$3
RELEASE_VERSION_QUALIFIER=$2
WORKSPACE=${WORKSPACE:-'.'}

if [ -z "$RELEASE_VERSION" ]; then
	echo "ERROR: Release version argument not supplied"
	exit 1
fi
if [ -z "$NEW_VERSION" ]; then
	echo "ERROR: New version argument not supplied"
	exit 1
fi

pushd $WORKSPACE &> /dev/null

# Update the versions in the build.xml file
sed -i 's@<property name="bv\.version\.spec" value=".*" />@<property name="bv.version.spec" value="'${RELEASE_VERSION%.*}'" />@' build.xml
if [ ! -z "$RELEASE_VERSION_QUALIFIER" ]; then
	# there is an issue with this specific expression if passed directly to sed
	sed_expression='s@<property name="bv\.version\.qualifier" value=".*" />@<property name="bv.version.qualifier" value="'${RELEASE_VERSION_QUALIFIER}'" />@'
	sed -i "${sed_expression}" build.xml
fi
sed -i 's@<property name="bv\.revdate" value=".*" />@<property name="bv.revdate" value="'$(date +%Y-%m-%d)'" />@' build.xml
sed -i 's@<property name="license" value=".*" />@<property name="license" value="license-final" />@' build.xml
sed -i 's@<property name="logo" value=".*" />@<property name="logo" value="jakarta_ee_logo_schooner_color_stacked_default.png[pdfwidth=4.25in,align=right]" />@' build.xml

git add build.xml
git commit -m "[Jenkins release job] Preparing release $RELEASE_VERSION"
git tag $RELEASE_VERSION

# Go back to a snapshot version
sed -i 's@<property name="bv\.version\.spec" value=".*" />@<property name="bv.version.spec" value="'${NEW_VERSION%.*}'" />@' build.xml
sed -i 's@<property name="bv\.version\.qualifier" value=".*" />@<property name="bv.version.qualifier" value="" />@' build.xml
sed -i 's@<property name="bv\.revdate" value=".*" />@<property name="bv.revdate" value="${current.date}" />@' build.xml
sed -i 's@<property name="license" value=".*" />@<property name="license" value="license-evaluation" />@' build.xml
sed -i 's@<property name="logo" value=".*" />@<property name="logo" value="beanvalidation_logo.png[align=left,pdfwidth=20%]" />@' build.xml

# Prepare next development iteration
git add build.xml
git commit -m "[Jenkins release job] Preparing next development iteration"

git push origin master
git push origin $RELEASE_VERSION

popd &> /dev/null

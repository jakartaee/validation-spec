#!/bin/bash
# Make sure there was no update to the used dependencies (if not, this is just a quick version check for Bundler)
ant all.doc
rc=$?
if [[ $rc != 0 ]] ; then
    echo "ERROR: Specification build failed!"
    exit $rc
fi

rsync --delete -avh build/en/html_single/ ci.hibernate.org:/var/www/staging-beanvalidation.org/latest-draft/spec/
rc=$?
if [[ $rc != 0 ]] ; then
    echo "ERROR: Specification sync failed!"
    exit $rc
fi

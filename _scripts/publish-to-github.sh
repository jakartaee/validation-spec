#! /bin/bash
ant all.doc
rc=$?
if [[ $rc != 0 ]] ; then
    echo "ERROR: Specification build failed!"
    exit $rc
fi

pushd build
# clone hibernate.github.io in _tmp if not present
if [ ! -d "beanvalidation.github.io" ];
then
  git clone --depth 1 git@github.com:beanvalidation/beanvalidation.github.io.git
  rc=$?
  if [[ $rc != 0 ]] ; then
    echo "ERROR: beanvalidation/beanvalidation.github.io cannot be cloned!"
    exit $rc
  fi
fi

cd beanvalidation.github.io
git fetch origin
git reset --hard origin/master

# Synchronize the content with the latest-draft folder of the site
rsync -av --delete --exclude ".git" ../en/html_single/ latest-draft/spec/ 
rc=$?
if [[ $rc != 0 ]] ; then
    echo "ERROR: Latest-draft sync failed!"
    exit $rc
fi

git add -A .
if git commit -m "Publish latest-draft of beanvalidation-spec" ;
then
 git push origin master;
 rc=$?
fi
popd
if [[ $rc != 0 ]] ; then
  echo "ERROR: Cannot push on site repository!"
  exit $rc
fi


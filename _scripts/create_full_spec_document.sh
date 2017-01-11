#!/bin/sh

DIR=$(dirname "$0")

cd $DIR/..

if [[ $(git status -s) ]]
then
    echo "The working directory is dirty. Please commit any pending changes."
    exit 1;
fi

branch="spec-full"
fullSpecDir="preprocessed"
workingDir="target/$fullSpecDir"
remote="origin"

echo "Deleting old worktree"
rm -rf $workingDir
mkdir $workingDir
git worktree prune
rm -rf .git/worktrees/$fullSpecDir

echo "Checking out $branch branch into $workingDir"
git fetch $remote
git worktree add -B $branch $workingDir $remote/$branch

echo "Generating full spec document"
ant generate-preprocessed

echo "Updating $branch branch"
cd $workingDir && git add --all && git commit -m "Publishing to $branch (create_full_spec_document.sh)"

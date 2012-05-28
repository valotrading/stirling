Releasing
=========

Creating a pull request for new release:

1. Checkout a new branch named `release/release-version`, e.g. `release/1.1.4`
2. Make sure Silvertip is updated to latest version in `build.sbt`
3. Update project version in `build.sbt`
4. Update version in `README.md`
5. Clone `gh-pages` branch of this repository to a sibling directory named `stirling-gh-pages`
6. Run `./sbt publish` to publish artifacts into `stirling-gh-pages`
7. Update project version in `build.sbt` to next snapshot version
8. Create a pull request for `release/1.1.4`

After the pull request is merged:

1. Create a tag from `master` branch, in this case `v1.1.4`
2. Push the `gh-pages` branch in `stirling-gh-pages`

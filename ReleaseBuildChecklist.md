# Introduction #

This is only intended for release builders of the KnitML Core Tools distribution.

# Details #

Pre-release:
  * create new versions / milestones in Bugzilla and change defaults
  * generate user's guides from Docbook
  * generate XML Schema documentation files
  * upload latest schemas to web site
  * upload latest samples to web site
  * upload latest docs to web site
  * update CHANGELOG.txt for this release
  * svn commit all changes
  * mvn release prepare (swear a lot)
  * grab distribution files on local workstation
  * unzip / rezip distribution
  * upload distribution to SourceForge
  * mvn release perform (swear even more)

Post-release:
  * Blog post
  * Ravelry post
  * Create new schemas for next target version
  * Upload new schemas to web site
  * Add entries to knitml.schemas
  * Change EL project schema object to use next version number
  * Change Core project schema object to use next version number
  * Update schema number in Eclipse tools (including KnitML catalog)
  * Update samples to use new schema
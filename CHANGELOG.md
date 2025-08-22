# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Show the available quota in the pull comments sidebar. (mddburgess/winston#80)

### Changed

- Refresh the available quota in the UI during pull comment requests. (mddburgess/winston#80)
- Highlight the available quota in yellow or red as it approaches zero. (mddburgess/winston#80)

### Fixed

- Execute pull operations in the order they appear in the pull request. (mddburgess/winston#83)

## [1.5.0] — 2025-08-20

### Added

- Videos on the channel page can now be selected for batch operations. (mddburgess/winston#47)
- Batch operation to pull comments and replies for all selected videos. (mddburgess/winston#47)
- Batch operation to pull comments and replies for all videos shown on the page. (mddburgess/winston#47)
- Comments can now be marked as important or hidden. (mddburgess/winston#77)

### Changed

- Comment statistics on the author list page now shows how many channels the author has commented on.
  (mddburgess/winston#77)
- Redesigned the author details page. (mddburgess/winston#77)

### Fixed

- Author's comments are now highlighted again on the author's detail page. (mddburgess/winston#69)

## [1.4.0] — 2025-07-27

### Added

- OpenAPI specification that documents the backend endpoints. (mddburgess/winston#70)

### Changed

- Updated backend to use code generated from the OpenAPI specification. (mddburgess/winston#70)
- Updated frontend to use code generated from the OpenAPI specification. (mddburgess/winston#70)
- Fetch comments action on the video page now also fetches all the replies. (mddburgess/winston#48)

## [1.3.0] — 2025-05-24

### Added

- Author summary endpoint that returns the channels and videos an author has commented on. (mddburgess/winston#45)
- New table and object property on videos to hold fetched comment counts, comments disabled flag, and last fetched date.
  (mddburgess/winston#46)

### Changed

- Show the channel handle in the channel details URI instead of the channel ID. (mddburgess/winston#50)
- Show the author handle in the author details URI instead of the author ID. (mddburgess/winston#50)
- Migrated build tool from Parcel to Vite. (mddburgess/winston#61)
- Redesigned author details page to reduce the amount of data requested from the backend. (mddburgess/winston#45)
- Show when comments have been fetched for a video and the fetch returned zero comments. (mddburgess/winston#46)

### Deprecated

- Deprecated the author details endpoint, to be replaced by the author summary endpoint. (mddburgess/winston#45)

### Fixed

- Comment and reply counts on the video details page now update in real time when comments and replies are fetched.
  (mddburgess/winston#43)
- Fixed N+1 query bug causing the comments API to execute slowly. (mddburgess/winston#44)

## [1.2.1] — 2025-04-16

### Fixed

- Fixed bug preventing comments from showing on the author details page. (mddburgess/winston#38)
- Page number is no longer reset when leaving and returning to a paginated view. (mddburgess/winston#21)

## [1.2.0] — 2025-04-15

### Added

- New fetch operation to fetch replies by parent comment ID. (mddburgess/winston#8)
- Clipboard icon on the video page to copy the video link to the clipboard. (mddburgess/winston#22)
- New fetch operation to fetch all replies to comments on a video. (mddburgess/winston#27)
- Button on the video page to fetch all replies to comments for that video. (mddburgess/winston#27)
- Remaining quota label to the header dropdown menu. (mddburgess/winston#35)

### Changed

- Make "more replies..." item a link that fetches replies to the comment. (mddburgess/winston#8)
- Clipboard icon on the channel page now copies the channel link to the clipboard. (mddburgess/winston#22)
- Video card now shows the fetched comment and reply counts. (mddburgess/#20)
- Show each author's comment statistics on the author list page. (mddburgess/winston#17)
- Show each author's profile image on the author list page. (mddburgess/winston#17)
- Menu bar in header is now a dropdown menu. (mddburgess/winston#35)

### Fixed

- Newly fetched channels no longer disappear after navigating away from the channel list page. (mddburgess/winston#23)
- Newly fetched videos no longer disappear after navigating away from the channel page. (mddburgess/winston#23)
- Newly fetched comments no longer disappear after navigating away from the video page. (mddburgess/winston#23)

## [1.1.0] — 2025-04-05

### Added

- Search box on channel page to search videos by title. (mddburgess/winston#7)
- Search box on video page to search comments by text or author. (mddburgess/winston#7)
- Pagination row at the bottom of the channel page after the video cards. (mddburgess/winston#7)
- Show in the UI when comments are disabled for a video. (mddburgess/winston#6)
- New page to list all authors. (mddburgess/winston#11)
- Navbar in the header with links to channel and author list pages. (mddburgess/winston#11)
- Breadcrumb on the author details page. (mddburgess/winston#11)

### Changed

- Show author's channel handle on the authors page instead of the ID. (mddburgess/winston#4)
- Group author's comments by video, and show the video card with the comments. (mddburgess/winston#4)
- Highlight the author's comments and replies. (mddburgess/winston#4)
- Render comments as HTML. (mddburgess/winston#5)
- Close event publishers early when shutting down the server. (mddburgess/winston#13)

### Fixed

- Fix `NullPointerException` that occurs when attempting to fetch a channel with a bad handle. (mddburgess/winston#14)

## [1.0.0] — 2025-03-29

### Added

- Fetch channel data from YouTube and store in the database.
- Fetch video data for a channel from YouTube and store in the database.
- Fetch comments for a video from YouTube and store in the database.
- Web UI to browse stored channels, videos, and comments.
- Web UI to fetch channels, videos, and comments.
- Cache channel and video thumbnails.

[Unreleased]: https://github.com/mddburgess/winston/compare/main...HEAD
[1.5.0]: https://github.com/mddburgess/winston/compare/v1.4.0...v1.5.0
[1.4.0]: https://github.com/mddburgess/winston/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/mddburgess/winston/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/mddburgess/winston/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/mddburgess/winston/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/mddburgess/winston/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/mddburgess/winston/releases/tag/v1.0.0

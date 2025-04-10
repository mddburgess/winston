# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- New fetch operation to fetch replies by parent comment ID. (mddburgess/winston#8)
- Clipboard icon on the video page to copy the video link to the clipboard. (mddburgess/winston#22)

### Changed

- Make "more replies..." item a link that fetches replies to the comment. (mddburgess/winston#8)
- Clipboard icon on the channel page now copies the channel link to the clipboard. (mddburgess/winston#22)
- Video card now shows the fetched comment and reply counts. (mddburgess/#20)
- Show each author's comment statistics on the author list page. (mddburgess/winston#17)

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

[Unreleased]: https://github.com/mddburgess/winston/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/mddburgess/winston/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/mddburgess/winston/releases/tag/v1.0.0

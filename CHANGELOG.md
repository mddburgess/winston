# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Improved API error responses for some common error cases. (mddburgess/winston#135)
- Migrated database entities and repositories to Kotlin. (mddburgess/winston#137)
- Migrated object converters to Kotlin. (mddburgess/winston#138)
- Enhanced error messages for validation errors. (mddburgess/winston#140)

### Fixed

- Fetch requests no longer loop infinitely when the next page token doesn't change. (mddburgess/winston#133)

## [1.8.0] — 2026-03-19

### Added

- Author search navigates to the author page when it finds one exact match. (mddburgess/winston#49)
- Button on the author details page to toggle "focus mode" for that author. (mddburgess/winston#123)
- Show focused author's statistics on the channel list cards and channel details page. (mddburgess/winston#123)
- Show focused author's statistics on the video list cards and video details page. (mddburgess/winston#123)
- Highlight focused author's comments on the video details page. (mddburgess/winston#123)
- Support for setting and removing aliases for an author. (mddburgess/winston#125)

### Changed

- Log method execution times only if they exceed a defined service level objective time. (mddburgess/winston#117)
- List authors endpoint returns a page of authors instead of the entire list. (mddburgess/winston#49)
- List authors endpoint supports searching for authors by display name. (mddburgess/winston#49)
- Authors page now searches for authors using the backend endpoint instead of in memory. (mddburgess/winston#49)
- Refactored API specification files. (mddburgess/winston#130)

### Removed

- Removed the ability to search comments on the video page. (mddburgess/winston#49)

### Fixed

- Sanitize comment strings before persisting to the database. (mddburgess/winston#113)
- Added indexes on authors and comments tables to fix slow queries on authors. (mddburgess/winston#119)
- Fixed slow query to get IDs of comments with missing replies. (mddburgess/winston#128)

## [1.7.0] — 2025-11-01

### Added

- Toggle to mark a channel as archived. (mddburgess/winston#105)
- Toggle to show or hide archived channels on the channel list page. (mddburgess/winston#105)
- Video cards are highlighted when the cursor hovers over them. (mddburgess/winston#108)
- UI control on the channel details page to choose how many videos to display per page. (mddburgess/winston#108)
- Endpoint to estimate the quota cost of a pull request. (mddburgess/winston#110)

### Changed

- Archived channels are hidden from the channel list page by default. (mddburgess/winston#105)
- Archived channels are shown faded in the channel list page. (mddburgess/winston#105)
- Backend videos API now returns paginated results instead of a full list. (mddburgess/winston#108)
- Updated channel details page to use the paginated videos API. (mddburgess/winston#108)
- The entire video card is now clickable, instead of just the title and image. (mddburgess/winston#108)
- Redesigned how to select videos for batch pulling comments. (mddburgess/winston#108)
- Pull requests first estimate the quota cost, and rejects if it exceeds the available quota. (mddburgess/winston#110)

### Fixed

- Optimized database queries for inserting and updating authors. (mddburgess/winston#86)
- Optimized database queries for inserting and updating comments. (mddburgess/winston#86)
- Added indexes on comments table to fix slow queries. (mddburgess/winston#110)

## [1.6.0] — 2025-09-14

### Added

- Show the available quota in the pull comments sidebar. (mddburgess/winston#80)
- Save the comment like counts when pulling comment data. (mddburgess/winston#88)
- Show the comment like counts in the UI. (mddburgess/winston#88)
- Show error messages on the pull channel modal. (mddburgess/winston#91)
- Show channel video, view, and subscriber counts on channel cards. (mddburgess/winston#91)
- Add button on channel details page to refresh channel data. (mddburgess/winston#92)
- Show channel statistics on channel details page. (mddburgess/winston#94)
- Add button on channel list page to refresh all channel data. (mddburgess/winston#93)
- Pull and store extended video data when pulling videos. (mddburgess/winston#99)
- Show extended video data on the video details page. (mddburgess/winston#99)

### Changed

- Refresh the available quota in the UI during pull comment requests. (mddburgess/winston#80)
- Highlight the available quota in yellow or red as it approaches zero. (mddburgess/winston#80)
- Highlight failed pull comments operations in yellow or red. (mddburgess/winston#79)
- Prevent pulling a channel that has already been pulled. (mddburgess/winston#91)
- Prevent attempting to pull a channel that had an error on a previous attempt. (mddburgess/winston#91)
- Pulling a channel no longer navigates to the channel page automatically on success. (mddburgess/winston#91)
- Redesigned the pull videos action on the channel details page. (mddburgess/winston#98)
- Use the playlist items API when pulling all videos for a channel. (mddburgess/winston#98)

### Fixed

- Execute pull operations in the order they appear in the pull request. (mddburgess/winston#83)
- Starting a pull comments batch now clears any existing video selection. (mddburgess/winston#82)
- Pull comments execution no longer stops on videos with comments disabled. (mddburgess/winston#79)
- Pull comments execution no longer stops on private or deleted videos. (mddburgess/winston#79)

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
[1.8.0]: https://github.com/mddburgess/winston/compare/v1.7.0...v1.8.0
[1.7.0]: https://github.com/mddburgess/winston/compare/v1.6.0...v1.7.0
[1.6.0]: https://github.com/mddburgess/winston/compare/v1.5.0...v1.6.0
[1.5.0]: https://github.com/mddburgess/winston/compare/v1.4.0...v1.5.0
[1.4.0]: https://github.com/mddburgess/winston/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/mddburgess/winston/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/mddburgess/winston/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/mddburgess/winston/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/mddburgess/winston/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/mddburgess/winston/releases/tag/v1.0.0

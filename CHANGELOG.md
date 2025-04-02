# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Search box on channel page to search videos by title. (mddburgess/winston#7)
- Search box on video page to search comments by text or author. (mddburgess/winston#7)
- Pagination row at the bottom of the channel page after the video cards. (mddburgess/winston#7)

### Changed

- Show author's channel handle on the authors page instead of the ID. (mddburgess/winston#4)
- Group author's comments by video, and show the video card with the comments. (mddburgess/winston#4)
- Highlight the author's comments and replies. (mddburgess/winston#4)
- Render comments as HTML. (mddburgess/winston#5)

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

[Unreleased]: https://github.com/mddburgess/winston/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/mddburgess/winston/releases/tag/v1.0.0

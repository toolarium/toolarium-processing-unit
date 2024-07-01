# toolarium-processing-unit

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [ 1.2.1 ] - 2024-07-01

## [ 1.2.0 ] - 2024-07-01
### Added
- Simplified parameter definitions by ParameterDefinitionBuilder.
- Copy constructor to ProcessingProgress.
- Added pure string constructor to ProcessingUnitProgressFormatter.
- Added isEmpty in IProcessingUnitContext.
- Added toString methods to ProcessingUnitUtil.
- Added TestProcessingUnitTests for test purpose.

### Changed
- Refactoring IProcessingUnit API.

### Fixed
- Nullpointer issues in case progress is null.
- ProgressStatistic.toString had invalid characters.

## [ 1.1.2 ] - 2024-06-29
### Added
- Added new interface for time measurement: IProcessingUnitRuntimeTimeMeasurement.
- Added ProcessingUnitProgressFormatter.

### Changed
- Added IProcessingUnitRuntimeTimeMeasurement to IProcessingUnitRunnableListener.

## [ 1.1.1 ] - 2024-06-28
### Changed
- Improved logging.
- Updated to toolarium-common:0.8.0.

### Fixed
- Removed Runnable from IProcessingUnitRunnable.

## [ 1.1.0 ] - 2024-06-23
### Added
- IProcessingProgress.resetProcessingStatusMessage to reset status messages.
- IProcessingStatistic.isEmpty method.
- ProcessingUnitRunnable.toString method to print proper the current status.
- Added test cases.

### Fixed
- Throttling issue after suspend and resume.
- Several null checks.

### Changed
- Improved logging.

## [ 1.0.0 ] - 2024-06-22
### Changed
- Finalized version 1.0.0 with unit tests and documentation.

## [ 0.9.0 ] - 2024-05-24
### Changed
- Refactoring API.

## [ 0.8.0 ] - 2024-04-30
### Changed
- Setup initial version.

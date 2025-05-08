# Raphigraphe

Raphigraphe is a command-line interface (CLI) tool for converting images into ASCII art using Braille characters. It processes images by cropping, resizing, applying thresholding, and mapping pixel data to Braille characters to generate ASCII art.

## Features

- Converts images to ASCII art using Braille characters.
- Supports cropping and resizing of images.
- Allows threshold adjustment for image processing.
- Outputs ASCII art directly to the console.

## Requirements

- Java 21 or higher
- Maven for building the project

## Installation

1. Clone the repository:

```bash
git clone https://github.com/memburg/raphigraphe.git
cd raphigraphe
```

2. Build the project using Maven:

```bash
mvn clean install
```

## Usage
Run the CLI tool with the following command:

```bash
java -jar target/raphigraphe-1.0-SNAPSHOT.jar <source-image-path> [options]
```

Parameters

- `<source-image-path>`: The path to the input image file to be converted.

Options
- -t, --threshold: The threshold value to be used for the conversion (default: 100).

Example:

```bash
java -jar target/raphigraphe-1.0-SNAPSHOT.jar src/main/resources/test-images/sample.png -t=120
```

This will load the image sample.png, process it, and output the ASCII art to the console.

## Project structure

```
raphigraphe/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/
│   │   │       └── github/
│   │   │           └── Raphigraphe.java
│   │   ├── resources/
│   │       ├── braille-characters/  # Braille character images
│   │       ├── output-results/      # Output directory for results
│   │       └── test-images/         # Sample test images
└── target/                          # Build output directory
```

## How It Works

1. Image Loading: The input image is loaded using the Imajine library.
2. Cropping: The image is cropped to focus on the central region.
3. Resizing: The cropped image is resized to a fixed width and height.
4. Thresholding: The image is thresholded to simplify pixel values.
5. ASCII Conversion: The image is divided into subimages, and each subimage is mapped to a Braille character based on pixel similarity.
6. Output: The ASCII art is printed to the console.

## Dependencies
The project uses the following dependencies:

- Imajine (v1.1.0): For image processing.
- Picocli (v4.7.7): For building the CLI interface.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
pdf-converter
===========
A Java library to convert .pdf files into .epub, .txt, .png, .jpg, .zip formats. The project follows the [Semantic Versioning Convention](http://semver.org/) and uses Apache 2.0 license.

[![Gitter Chat](http://img.shields.io/badge/chat-online-brightgreen.svg)](https://gitter.im/pdf-converter/)
[![Build Status](https://travis-ci.org/jmrozanec/pdf-converter.svg?branch=master)](https://travis-ci.org/jmrozanec/pdf-converter)
[![Coverage Status](https://coveralls.io/repos/github/jmrozanec/pdf-converter/badge.svg?branch=master)](https://coveralls.io/github/jmrozanec/pdf-converter?branch=master)

**Download**

pdf-converter is available on [Maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.bit-scout%22) repository.

    <dependency>
        <groupId>com.bit-scout</groupId>
        <artifactId>pdf-converter</artifactId>
        <version>1.0.2</version>
    </dependency>

**Features**

Any .pdf file can be converted to the following formats:
 * .epub: output file can either contain images of .pdf pages or their transcript.
 * .txt: contains the transcript of the .pdf file
 * .png: all pages converted to .png images
 * .jpg: all pages converted to .jpg images
 * .zip: contains images for all pages from the .pdf in the original resolution. Images can be either in .png or .jpg format.
 
 
**Example**
 
```java

    PdfConverter
        .convert(new File(mobydick.pdf))
        .intoEpub("Moby Dick", new File("mobydick.epub"));
```

**Contribute & Support!**

Contributions are welcome! You can contribute by
 * starring and/or Flattring this repo!
 * requesting or adding new features.
 * enhancing existing code: ex.: provide more accurate description cases
 * testing
 * enhancing documentation
 * providing translations to support new locales
 * bringing suggestions and reporting bugs
 * spreading the word / telling us how you use it!

Support us donating once or by subscription through Flattr!

[![Flattr this!](https://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=jmrozanec&url=https://github.com/jmrozanec/pdf-converter)

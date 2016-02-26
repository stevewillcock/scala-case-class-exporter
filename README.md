# scala-case-class-exporter

Simple utility to export Scala case classes to C# types.

This may be used for simple interoperability when calling akka-http services from C# code without manually coding types or generating types from swagger specifications for example.

To produce a jar with all dependencies bundled, start sbt and run

    assembly

See run.sh for a usage example.

## TODO

- Include namespace for input class identifiers
- Support wildcards as part of input class identifiers
- Support output namespace as an option
- Add output options for other languages
  - Typescript
  - Java
  - F#
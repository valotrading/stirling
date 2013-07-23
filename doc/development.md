Development
===========

IDEs
----

Stirling does not provide SBT plugins for [Eclipse][], [IDEA][] or other IDEs.
If you want to use an IDE, we recommend adding the SBT plugin for the IDE into
the [global plugin definitions][SBT]. Likewise, we recommend adding the IDE
artifacts that should not be under version control into a [global .gitignore
file][Git].

  [Eclipse]: http://eclipse.org/
  [IDEA]:    http://jetbrains.com/idea/

  [SBT]: http://www.scala-sbt.org/release/docs/Extending/Plugins#global-plugins
  [Git]: https://help.github.com/articles/ignoring-files#global-gitignore

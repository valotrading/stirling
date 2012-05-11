Implementation
==============

FIX
---

### FIX Protocol Profiles

In order to support messages of various FIX protocol profiles, Stirling allows
definition of message classes that are profile specific. In most cases, a
profile specific FIX message comprises of fields, which can be categorized by
their value as follows: (a) the values of a field is fully specified by the
standard, (b) the values of a field is a subset of the standard, or (c) the
values of a field is not defined in the standard and may assign another meaning
for a value that is specified in the standard.

If the field is defined as it is defined in the standard, then the standard
field type is used. The fields following the standard are placed under the
`stirling.fix.tags` package.

A profile specific enumeration is introduced when the standard defines a string
field and the profile specifies an enumeration of possible values. A profile
specific enumeration is also introduced when the profile uses non-standard
values in the enumeration. However, if the profile uses a subset of an
enumeration, Stirling uses the standard field type.

In cases where a field is a combination of multiple characters, a field is
based on the string field type and the validation of such fields is left for
the user.

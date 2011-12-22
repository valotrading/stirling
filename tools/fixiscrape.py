#!/usr/bin/env python3
import httplib2
import sys
from optparse import OptionParser
from lxml import etree
from collections import namedtuple
def main():
    parser = init_option_parser()
    (options, args) = parser.parse_args()
    if len(args) == 0:
        parser.print_usage()
    tagnumbers = sorted(set([int(val) for val in args]))
    fetch_and_print_tag_definitions(tagnumbers, options.comments, options.versiondelims, options.includepre44)

def init_option_parser():
    parser = OptionParser(usage="Usage: %prog [options] [tagnumbers...]")
    parser.add_option("-c","--no-comments", dest="comments",
                      action="store_false", default=True,
                      help="don't print comments")
    parser.add_option("-v","--no-version-delimiters", dest="versiondelims",
                      action="store_false", default=True,
                      help="don't print version delimiter comments")
    parser.add_option("-4","--fix44-or-newer", dest="includepre44",
                      action="store_false", default=True,
                      help="Use FIX versions 4.4 and newer. This is useful because enum value names are valid identifiers in FIXimate in FIX versions 4.4 and newer.")
    return parser

def fetch_and_print_tag_definitions(tagnumbers, comments, versiondelims, includepre44):
    tags = MultiDict()
    for tagnumber in tagnumbers:
        (fixversion, objectdefinition, comment) = fetch_tag_version_and_definition(tagnumber, includepre44)
        tags[fixversion] = (objectdefinition, comment)
    for fixversion in sorted(tags.keys()):
        if versiondelims:
            print("// FIX {}".format(fixversion))
        for object_def, comment in tags[fixversion]:
            print("".join((object_def, comment)) if comments else object_def)

class MultiDict(dict):
    def __setitem__(self, item, value):
        if not item in self:
            super(MultiDict, self).__setitem__(item, [])
        self[item].append(value)

def fetch_tag_version_and_definition(tagnumber, includepre44):
    known_fix_versions = ["4.0","4.1","4.2","4.3"] if includepre44 else []
    known_fix_versions.extend(("4.4","5.0","5.0SP1","5.0SP2"))

    def define_ordinary_tag(taginfo):
        tag_types = {
            "Currency":"CurrencyTag",
            "Exchange":"ExchangeTag",
            "LocalMktDate":"LocalMktDateTag",
            "NumInGroup":"NumInGroupTag",
            "Price":"PriceTag",
            "Qty":"QtyTag",
            "String":"StringTag",
            "char":"CharTag",
            "float":"FloatTag",
            "int":"IntegerTag"
            }
        ourtagtype = tag_types[taginfo.tagtype]
        template = "object {} extends {}({})"
        return template.format(taginfo.tagname,
                                      ourtagtype,
                                      taginfo.tagnumber)

    def define_enum_tag(taginfo):
        enum_types = {
            "char":"EnumTag[Character]",
            "int":"EnumTag[Integer]",
            "String":"EnumTag[String]",
            "Boolean":"EnumTag[Boolean]"
            }

        def enum_contents(taginfo):
            def parse_enum_row(row):
                enumTemplates = {
                    "char":"'{}'",
                    "int":"{}",
                    "String":"\"{}\""
                    }
                tabledatas = list(row)
                template = enumTemplates[taginfo.tagtype]
                value = template.format(tabledatas[0].text)
                name = tabledatas[2].findtext("./p/span")
                return "  val {} = Value({})".format(name, value)

            parsed_enums = [parse_enum_row(row)
                            for row
                            in taginfo.enumcontents]
            return "\n".join(parsed_enums)
        enum_template = """object {} extends {}({}) {{
{}
}}"""
        return enum_template.format(taginfo.tagname,
             enum_types[taginfo.tagtype],
             taginfo.tagnumber,
             enum_contents(taginfo))

    def parse_tag(content):
        tree = etree.fromstring(content)
        TagInfo = namedtuple('TagInfo','tagname tagnumber tagtype enumcontents')
        taginfo = TagInfo(tagname =  tree.findtext('.//td[@class="FldNameEven"]/a'),
                          tagnumber = tree.findtext('.//td[@class="FldTagEven"]/a'),
                          tagtype = tree.findtext('.//td[@class="FldDatEven"]'),
                          enumcontents = tree.findall('.//td[@class="FldEven"]/table/tr'))
        return taginfo

    h = httplib2.Http('.cache')
    url_template = 'http://www.fixprotocol.org/FIXimate3.0/en/FIX.{}/tag{}.html'
    for fixversion in known_fix_versions:
        url = url_template.format(fixversion, str(tagnumber))
        response, content = h.request(url)
        if response['status'] != "404":
            taginfo = parse_tag(content)
            comment = " // since FIX {}: {}".format(fixversion, url)
            is_enum = len(taginfo.enumcontents) > 0
            objectdefinition = define_ordinary_tag(taginfo) if not is_enum else define_enum_tag(taginfo)
            return (fixversion, objectdefinition, comment)
    return ("NONE","tagnumber " + str(tagnumber) +
            " not found in fix versions " +
            str(known_fix_versions), "")

if __name__ == "__main__":
    main()

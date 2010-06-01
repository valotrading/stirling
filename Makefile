JAVAC		:= ecj
JAVA		:= java
JAVAC_OPTS	:= -1.6 -encoding utf8
JAR		:= jar

# Make the build silent by default
V =
ifeq ($(strip $(V)),)
  E = @echo
  Q = @
else
  E = @\#
  Q =
endif
export E Q

SRC_DIR := src
BUILD_DIR := build

LIBRARY	:= fixengine.jar

DEPS :=							\
	lib/runtime/commons-lang/commons-lang-2.4.jar	\
	lib/runtime/joda-time/joda-time-1.5.2.jar	\
	lib/runtime/log4j/log4j-1.2.15.jar		\

TEST_DEPS := \
	$(LIBRARY) \
	lib/test/junit/junit-4.4.jar \
	lib/test/jmock/bsh-core-2.0b4.jar \
	lib/test/jmock/cglib-2.1_3-src.jar \
	lib/test/jmock/cglib-nodep-2.1_3.jar \
	lib/test/jmock/hamcrest-core-1.1.jar \
	lib/test/jmock/hamcrest-library-1.1.jar \
	lib/test/jmock/jmock-2.5.1.jar \
	lib/test/jmock/jmock-junit3-2.5.1.jar \
	lib/test/jmock/jmock-junit4-2.5.1.jar \
	lib/test/jmock/jmock-legacy-2.5.1.jar \
	lib/test/jmock/jmock-script-2.5.1.jar \
	lib/test/jmock/objenesis-1.0.jar \
	lib/test/jdave/jdave-core-1.1-rc1.jar \
	lib/test/jdave/jdave-junit4-1.1-rc1.jar

BUILD_CLASSPATH := $(subst .jar ,.jar:,$(DEPS))

TEST_CLASSPATH := $(BUILD_CLASSPATH):$(subst .jar ,.jar:,$(TEST_DEPS)):$(BUILD_DIR)/test

TESTS := $(shell find test -name "*Spec.java" | sed -e 's/^test\///' -e 's/\.java$$//' -e 's/\//./g')

all: $(LIBRARY) test
.DEFAULT: all
.PHONY: all

$(LIBRARY):
	$(Q) rm -rf $(BUILD_DIR)
	$(Q) mkdir -p $(BUILD_DIR)/$(LIBRARY)
	$(E) "  JAVAC"
	$(Q) $(JAVAC) $(JAVAC_OPTS) -cp $(BUILD_CLASSPATH) $(SRC_DIR)/ -d $(BUILD_DIR)/$(LIBRARY)
	$(E) "  JAR     " $@
	$(Q) $(JAR) cf $(LIBRARY) -C $(BUILD_DIR)/$(LIBRARY)/ .

test: $(LIBRARY)
	$(Q) mkdir -p $(BUILD_DIR)/test
	$(E) "  JAVAC"
	$(Q) $(JAVAC) $(JAVAC_OPTS) -cp $(TEST_CLASSPATH) test/ -d $(BUILD_DIR)/test
	$(E) "  TEST"
	$(Q) $(JAVA) -cp $(TEST_CLASSPATH) org.junit.runner.JUnitCore $(TESTS)
.PHONY: test

clean:
	$(E) "  CLEAN"
	$(Q) rm -rf $(BUILD_DIR)
	$(Q) rm -f $(LIBRARY)
.PHONY: clean

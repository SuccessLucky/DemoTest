package com.kzksmarthome.common.lib.easylink.modle;

public class KT {
        private String name;
        private String range;

        public KT(String name, String range) {
            this.name = name;
            this.range = range;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }
    }
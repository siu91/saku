DROP SEQUENCE IF EXISTS "saku_url_map_seq" CASCADE;
CREATE SEQUENCE "saku_url_map_seq"
INCREMENT 1
MINVALUE  1000
MAXVALUE 9223372036854775807
START 1000
CACHE 1;

DROP SEQUENCE IF EXISTS "saku_shorturl_map_seq" CASCADE;
CREATE SEQUENCE "saku_shorturl_map_seq"
INCREMENT 1
MINVALUE  1000
MAXVALUE 9223372036854775807
START 1000
CACHE 1;

DROP SEQUENCE IF EXISTS "saku_pre_register_seq" CASCADE;
CREATE SEQUENCE "saku_pre_register_seq"
INCREMENT 1
MINVALUE  1000
MAXVALUE 9223372036854775807
START 1000
CACHE 1;


-- ----------------------------
-- Table structure for saku_pre_register
-- ----------------------------
DROP TABLE IF EXISTS "saku_pre_register";
CREATE TABLE "saku_pre_register" (
  "id" int8 NOT NULL DEFAULT nextval('saku_pre_register_seq'::regclass),
  "start_no" int8 NOT NULL,
  "end_no" int8 NOT NULL,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "saku_pre_register"."id" IS '主键';
COMMENT ON COLUMN "saku_pre_register"."start_no" IS '开始编号';
COMMENT ON COLUMN "saku_pre_register"."end_no" IS '结束编号';
COMMENT ON COLUMN "saku_pre_register"."create_time" IS '创建时间';

-- ----------------------------
-- Primary Key structure for table saku_pre_register
-- ----------------------------
ALTER TABLE "saku_pre_register" ADD CONSTRAINT "saku_pre_register_pkey" PRIMARY KEY ("id");



-- ----------------------------
-- Table structure for saku_shorturl_map
-- ----------------------------
DROP TABLE IF EXISTS "saku_shorturl_map";
CREATE TABLE "saku_shorturl_map" (
  "id" int8 NOT NULL DEFAULT nextval('saku_shorturl_map_seq'::regclass),
  "l_url" varchar(255) COLLATE "pg_catalog"."default",
  "l_url_md5" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "saku_shorturl_map"."id" IS '短链ID（自增生成）';
COMMENT ON COLUMN "saku_shorturl_map"."l_url" IS '长链';
COMMENT ON COLUMN "saku_shorturl_map"."l_url_md5" IS '长链MD5';
COMMENT ON COLUMN "saku_shorturl_map"."create_time" IS '创建时间';



-- ----------------------------
-- Table structure for saku_url_map
-- ----------------------------
DROP TABLE IF EXISTS "saku_url_map";
CREATE TABLE "saku_url_map" (
  "id" int8 NOT NULL DEFAULT nextval('saku_url_map_seq'::regclass),
  "l_url" varchar(255) COLLATE "pg_catalog"."default",
  "s_url" varchar(12) COLLATE "pg_catalog"."default" DEFAULT nextval('saku_url_map_seq'::regclass),
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "saku_url_map"."id" IS 'ID';
COMMENT ON COLUMN "saku_url_map"."l_url" IS '长链';
COMMENT ON COLUMN "saku_url_map"."s_url" IS '短链';
COMMENT ON COLUMN "saku_url_map"."create_time" IS '创建时间';

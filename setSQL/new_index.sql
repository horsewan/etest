-- 增加某些表的索引，以提高查询速度

ALTER TABLE `dy_business_info`
	ADD UNIQUE INDEX `index_b_ticket` (`b_ticket`),
	ADD INDEX `index_b_phone` (`b_phone`),
	ADD UNIQUE INDEX `index_b_name` (`b_name`(64));


ALTER TABLE `dy_orders`
	ADD INDEX `index_uid` (`uid`),
	ADD INDEX `index_business_id` (`business_id`);


ALTER TABLE `dy_orders_detail`
	ADD INDEX `index_order_number` (`order_number`);
	
	
ALTER TABLE `dy_orders_pay`
	ADD INDEX `index_order_number` (`order_number`);

	
ALTER TABLE `nq_exp_info`
	ADD INDEX `index_uid` (`uid`);
	
	
ALTER TABLE `nq_user_friend`
	ADD INDEX `index_user_id` (`user_id`),
	ADD INDEX `index_friend_mobile` (`friend_mobile`),
	ADD INDEX `index_user_friend_id` (`user_friend_id`);


ALTER TABLE `nq_user_info`
	DROP INDEX `index_mobile`,
	ADD INDEX `index_nick_name` (`nick_name`),
	ADD UNIQUE INDEX `index_mobile` (`mobile`),
	ADD INDEX `index_agent_no` (`agent_no`);


ALTER TABLE `nq_user_order_address`
	ADD INDEX `index_order_number` (`order_number`);
	
	
ALTER TABLE `nq_user_vip_record`
	ADD INDEX `index_uid` (`uid`);


ALTER TABLE `sys_city`
	ADD INDEX `index_city_name` (`city_name`);


ALTER TABLE `nq_user_address`
	ADD INDEX `index_uid` (`uid`);


package com.mobile.hotel.model

/**
{
    "yunos_ebanma_right_platform_call_response": {
        "message": "成功",
        "status": "0",
        "trace_id": "0b84d3e417567073606043110e2188",
        "value": "{\"data\":[{\"address\":\"武汉市洪山区洪山乡南湖村宝安中海公寓D栋203室\",\"companyId\":72319048,\"companyName\":\"风米酒店(建安街地铁站店)\",\"grade\":\"2\",\"images\":[\"https://img.alicdn.com/imgextra/i1/6000000001892/O1CN01vYTv7W1Pqa8NZkDMU_!!6000000001892-0-hotel.jpg\"],\"latitude\":30.498756,\"longitude\":114.314261,\"minPrice\":104,\"openDate\":\"2022\",\"repairDate\":\"2022\",\"score\":\"4.6\",\"services\":[\"信用住\",\"榻榻米\",\"自动贩卖机\"],\"tel\":\"18771050547\"},{\"address\":\"武汉-武昌区-丁字桥南路与富安街交汇处水域天际49栋商铺-，近武昌火车站。\",\"companyId\":51927026,\"companyName\":\"雅斯特酒店(武汉南湖理工大学佰港城店)\",\"grade\":\"3\",\"images\":[\"https://img.alicdn.com/imgextra/i3/6000000001359/O1CN01BMCl061LuT0DFnJex_!!6000000001359-0-hotel.jpg\"],\"latitude\":30.500607425576543,\"longitude\":114.3249482732635,\"minPrice\":183,\"openDate\":\"2016\",\"repairDate\":\"2019\",\"score\":\"4.3\",\"services\":[\"信用住\",\"免费熨衣\",\"商业区周边\"],\"tel\":\"027-88607888\"},{\"address\":\"武汉武昌区长虹桥113号\",\"companyId\":51896010,\"companyName\":\"空杯连锁酒店(南湖建安街武汉理工大店)\",\"grade\":\"2\",\"images\":[\"https://img.alicdn.com/imgextra/i3/6000000007373/O1CN01otJD2m24KtBdtpf1o_!!6000000007373-0-hotel.jpg\"],\"latitude\":30.506826,\"longitude\":114.309358,\"minPrice\":176,\"openDate\":\"2016\",\"repairDate\":\"2023\",\"score\":\"4.6\",\"services\":[\"信用住\",\"精选推荐\",\"简约风\",\"有麻将房\"],\"tel\":\"17762580101\"},{\"address\":\"武汉洪山区丁字桥南路维佳佰港城B座25楼2513室, 中国\",\"companyId\":76757282,\"companyName\":\"苏安家酒店公寓(南湖佰港城店)\",\"grade\":\"2\",\"images\":[\"https://img.alicdn.com/imgextra/i3/6000000001794/O1CN01v8flVI1P7hTphy9Gg_!!6000000001794-2-hotel.png\"],\"latitude\":30.50334900143759,\"longitude\":114.32655962203533,\"minPrice\":165,\"openDate\":\"2023\",\"repairDate\":\"2023\",\"score\":\"4.9\",\"services\":[\"信用住\",\"电动按摩椅\",\"1080P投影仪\"],\"tel\":\"18971667388\"},{\"address\":\"文昌路30号，近泛悦MALL，文昌路地铁站\",\"companyId\":55571311,\"companyName\":\"武汉维佳青舍酒店（武汉理工大学佰港城店）\",\"grade\":\"4\",\"images\":[\"https://img.alicdn.com/imgextra/i4/6000000003795/O1CN01uwsP461du9ysWGzxh_!!6000000003795-0-hotel.jpg\"],\"latitude\":30.50274,\"longitude\":114.328083,\"minPrice\":272,\"openDate\":\"2018\",\"repairDate\":\"2017\",\"score\":\"4.7\",\"services\":[\"信用住\",\"商业区周边\",\"茶室\"],\"tel\":\"027-87569999\"},{\"address\":\"南湖花园富安街南湖名都B区（南湖一小斜对面）\",\"companyId\":50943044,\"companyName\":\"城市便捷酒店(南湖建安街地铁站佰港城店)\",\"grade\":\"3\",\"images\":[\"https://img.alicdn.com/imgextra/i4/6000000001681/O1CN014hOm811OHwm2GjQXW_!!6000000001681-0-hotel.jpg\"],\"latitude\":30.50076,\"longitude\":114.315401,\"minPrice\":173,\"openDate\":\"2020\",\"repairDate\":\"2020\",\"score\":\"4.9\",\"services\":[\"商业区周边\",\"旅游票务服务\"],\"tel\":\"027-88028000\"},{\"address\":\"富安街34附123号\",\"companyId\":72832959,\"companyName\":\"萤火电竞酒店(武汉南湖沃尔玛店)\",\"grade\":\"2\",\"images\":[\"https://img.alicdn.com/imgextra/i2/6000000002533/O1CN01mePKDF1UaA7sFpcFk_!!6000000002533-0-hotel.jpg\"],\"latitude\":30.50025,\"longitude\":114.324539,\"minPrice\":171,\"openDate\":\"2022\",\"repairDate\":\"2022\",\"services\":[\"酷睿i7 CPU\",\"16G内存\"],\"tel\":\"027-88229788\"},{\"address\":\"徐东大街160号\",\"companyId\":10060509,\"companyName\":\"武汉光明万丽酒店\",\"grade\":\"5\",\"images\":[\"https://img.alicdn.com/imgextra/i4/6000000007863/O1CN01t7tN9S27xJFDBQHaI_!!6000000007863-0-hotel.jpg\"],\"latitude\":30.579513,\"longitude\":114.356249,\"minPrice\":450,\"openDate\":\"2007\",\"repairDate\":\"2018\",\"score\":\"4.6\",\"services\":[\"信用住\",\"恒温泳池\",\"工业风\"],\"tel\":\"027-86621388\"}],\"hasNextPage\":true,\"offset\":11}",
        "request_id": "16kdesnqj90tv"
    }
}
*/
data class BaseModel(
    val yunos_ebanma_right_platform_call_response: YunosEbanmaRightPlatformCallResponse? = YunosEbanmaRightPlatformCallResponse()
) {
    data class YunosEbanmaRightPlatformCallResponse(
        val message: String? = "",
        val request_id: String? = "",
        val status: String? = "",
        val trace_id: String? = "",
        val value: String? = ""
    )
}
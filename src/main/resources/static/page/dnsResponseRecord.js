let dnsResponseRecord = {
    "type": "page",
    "body": [
        {
            "label": "新增",
            "type": "action",
            "level": "info",
            "actionType": "dialog",
            "className": "m-b-sm",
            "dialog": {
                "title": "新增",
                "body": {
                    "type": "form",
                    "api": "post:/dnsResponseRecord/add",
                    "controls": [
                        {
                            "type": "text",
                            "name": "name",
                            "label": "样例",
                            "required": true
                        },
                    ],
                }
            },
        },
        {
            "type": "crud",
            "name": "dnsResponseRecordCrud",
            "api": "get:/dnsResponseRecord/getList",
            "quickSaveItemApi": "put:/dnsResponseRecord/update",
            "saveOrderApi": "get:/dnsResponseRecord/changeOrder?ids=${ids}",
            "syncLocation": false,
            "affixHeader": false,
            "footable": true,
            "headerToolbar": [
                "filter-toggler",
                "bulkActions",
                "statistics"
            ],
            "footerToolbar": [
                "switch-per-page",
                "pagination"
            ],
            "bulkActions": [
                {
                    "label": "批量删除",
                    "level": "danger",
                    "actionType": "ajax",
                    "api": "delete:/dnsResponseRecord/delete?ids=${ids}",
                    "confirmText": "确定要批量删除吗?",
                },
            ],
            "filter": {
                "title": "条件搜索",
                "mode": "horizontal",
                "horizontal": {
                    "leftFixed": true
                },
                "controls": [
                    {
                        "type": "group",
                        "controls": [
                            {
                                "type": "text",
                                "name": "qname",
                                "label": "域名",
                            },
                            {
                                "type": "select",
                                "name": "qtype",
                                "label": "类型",
                                "source": "get:/dnsRecord/getQTypeOptionList",
                            },
                            {
                                "type": "text",
                                "name": "value",
                                "label": "记录值",
                            },
                        ]
                    },
                    {
                        "type": "group",
                        "controls": [
                            {
                                "type": "select",
                                "name": "rcode",
                                "label": "返回码",
                                "source": "get:/dnsRecord/getRCODEOptionList"
                            },
                            {
                                "type": "select",
                                "name": "ra",
                                "label": "是否递归查询",
                                "options": [
                                    {
                                        "label": "全部",
                                        "value": 0
                                    },
                                    {
                                        "label": "非递归查询",
                                        "value": 1
                                    },
                                    {
                                        "label": "递归查询",
                                        "value": 2
                                    },
                                ],
                                "value": 0
                            },
                            {
                                "type": "select",
                                "name": "aa",
                                "label": "是否权威查询",
                                "options": [
                                    {
                                        "label": "全部",
                                        "value": 0
                                    },
                                    {
                                        "label": "非权威回答",
                                        "value": 1
                                    },
                                    {
                                        "label": "权威回答",
                                        "value": 2
                                    },
                                ],
                                "value": 0
                            },
                        ]
                    },
                ],
            },
            "draggable": true,
            "columns": [
                {
                    "name": "id",
                    "label": "ID",
                },
                {
                    "name": "dnsRequestRecordId",
                    "label": "关联DNS请求id",
                },
                {
                    "name": "transactionId",
                    "label": "标识id",
                },
                {
                    "name": "qr",
                    "label": "查询类型",
                    "breakpoint": "*"
                },
                {
                    "name": "opcode",
                    "label": "操作类型",
                    "breakpoint": "*"
                },
                {
                    "type": "status",
                    "name": "aa",
                    "label": "是否权威回答",
                },
                {
                    "type": "status",
                    "name": "tc",
                    "label": "是否截断",
                    "breakpoint": "*"
                },
                {
                    "name": "rd",
                    "label": "客户端请求递归",
                    "breakpoint": "*"
                },
                {
                    "name": "ra",
                    "label": "服务端响应递归",
                    "breakpoint": "*"
                },
                {
                    "name": "rcode",
                    "label": "响应类型",
                },
                {
                    "name": "qname",
                    "label": "域名",
                },
                {
                    "name": "qtype",
                    "label": "类型",
                },
                {
                    "name": "ttl",
                    "label": "资源缓存时间(秒)",
                },
                {
                    "name": "value",
                    "label": "数据",
                },
                {
                    "name": "resourceRecordType",
                    "label": "类型",
                },
                {
                    "name": "createdTime",
                    "label": "创建时间",
                    "type": "datetime",
                    "sortable": true
                },
                {
                    "name": "updatedTime",
                    "label": "更新时间",
                    "type": "datetime",
                    "breakpoint": "*"
                },
                {
                    "label": "操作",
                    "type": "operation",
                    'buttons': [
                        {
                            "label": "删除",
                            "type": "button",
                            "level": "danger",
                            "actionType": "ajax",
                            "api": "delete:/dnsResponseRecord/delete?ids=${id}",
                            "confirmText": "确认删除吗?"
                        },
                    ]
                }
            ]
        }]
};
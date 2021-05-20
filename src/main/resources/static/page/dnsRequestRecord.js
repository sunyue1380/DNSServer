let dnsRequestRecord = {
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
                    "api": "post:/dnsRequestRecord/add",
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
            "name": "dnsRequestRecordCrud",
            "api": "get:/dnsRequestRecord/getList",
            "quickSaveItemApi": "put:/dnsRequestRecord/update",
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
                    "api": "delete:/dnsRequestRecord/delete?ids=${ids}",
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
                                "name": "ip",
                                "label": "ip",
                            },
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
                    "name": "ip",
                    "label": "来源IP地址",
                },
                {
                    "name": "port",
                    "label": "客户端端口",
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
                    "breakpoint": "*"
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
                },
                {
                    "name": "ra",
                    "label": "服务端响应递归",
                    "breakpoint": "*"
                },
                {
                    "name": "rcode",
                    "label": "响应类型",
                    "breakpoint": "*"
                },
                {
                    "name": "qtype",
                    "label": "问题类型",
                },
                {
                    "name": "qname",
                    "label": "域名",
                },
                {
                    "name": "value",
                    "label": "结果",
                    "breakpoint": "*"
                },
                {
                    "name": "qclass",
                    "label": "查询协议类",
                    "breakpoint": "*"
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
                    "sortable": true
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
                            "api": "delete:/dnsRequestRecord/delete?ids=${id}",
                            "confirmText": "确认删除吗?"
                        },
                    ]
                }
            ]
        }]
};
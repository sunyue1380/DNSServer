let dnsErrorDatagram = {
    "type": "page",
    "body": [
        {
            "type": "crud",
            "name": "dnsErrorDatagramCrud",
            "api": "get:/dnsErrorDatagram/getList",
            "quickSaveItemApi": "put:/dnsErrorDatagram/update",
            "saveOrderApi": "get:/dnsErrorDatagram/changeOrder?ids=${ids}",
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
                    "api": "delete:/dnsErrorDatagram/delete?ids=${ids}",
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
                                "type": "select",
                                "name": "normal",
                                "label": "状态",
                                "options": [
                                    {
                                        "label": "全部",
                                        "value": 0
                                    },
                                    {
                                        "label": "异常",
                                        "value": 1
                                    },
                                    {
                                        "label": "正常",
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
                    "type": "status",
                    "name": "normal",
                    "label": "是否正常解析",
                },
                {
                    "name": "message",
                    "label": "异常提示",
                },
                {
                    "name": "createdTime",
                    "label": "创建时间",
                    "type": "datetime",
                    "breakpoint": "*"
                },
                {
                    "name": "updatedTime",
                    "label": "更新时间",
                    "type": "datetime",
                    "breakpoint": "*"
                },
                {
                    "type": "operation",
                    'buttons': [
                        {
                            "label": "解析",
                            "type": "button",
                            "level": "info",
                            "actionType": "ajax",
                            "api": "put:/dnsErrorDatagram/analyze?ids=${id}",
                            "confirmText": "确认解析吗?"
                        },
                        {
                            "label": "查看报文",
                            "type": "button",
                            "level": "info",
                            "actionType": "dialog",
                            "dialog": {
                                "title": "报文信息",
                                "body": {
                                    "type": "property",
                                    "title": "报文信息",
                                    "items": [
                                        {
                                            "label": "报文信息",
                                            "content": "${content | raw}"
                                        }
                                    ]
                                }
                            },
                            "visibleOn": "this.normal"
                        },
                    ]
                }
            ]
        }]
};
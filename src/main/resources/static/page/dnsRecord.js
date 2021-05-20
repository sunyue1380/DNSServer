let dnsRecord = {
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
                    "api": "post:/dnsRecord/add",
                    "controls": [
                        {
                            "type": "text",
                            "name": "domain",
                            "label": "域名",
                            "required": true
                        },
                        {
                            "type": "select",
                            "name": "type",
                            "source": "get:/dnsRecord/getQTypeOptionList",
                            "label": "类型",
                            "required": true
                        },
                        {
                            "type": "number",
                            "name": "ttl",
                            "label": "缓存时间(秒)",
                            "required": true,
                            "value": 600
                        },
                        {
                            "type": "text",
                            "name": "value",
                            "label": "记录值",
                            "required": true
                        }
                    ],
                }
            },
        },
        {
            "type": "crud",
            "name": "dnsRecordCrud",
            "api": "get:/dnsRecord/getList",
            "quickSaveItemApi": "put:/dnsRecord/update",
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
                    "api": "delete:/dnsRecord/delete?ids=${ids}",
                    "confirmText": "确定要批量删除吗?",
                },
            ],
            "columns": [
                {
                    "name": "id",
                    "label": "ID",
                },
                {
                    "type": "link",
                    "name": "domain",
                    "label": "域名",
                    "body": "${domain}",
                    "href": "http://${domain}",
                    "blank": true
                },
                {
                    "name": "type",
                    "label": "类型",
                },
                {
                    "name": "ttl",
                    "label": "缓存时间(秒)",
                },
                {
                    "name": "value",
                    "label": "记录值",
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
                            "label": "编辑",
                            "type": "button",
                            "level": "info",
                            "actionType": "drawer",
                            "drawer": {
                                "title": "编辑信息",
                                "body": {
                                    "type": "form",
                                    "api": "put:/dnsRecord/update",
                                    "controls": [
                                        {
                                            "type": "hidden",
                                            "name": "id",
                                            "label": "id",
                                        },
                                        {
                                            "type": "text",
                                            "name": "domain",
                                            "label": "域名",
                                            "required": true
                                        },
                                        {
                                            "type": "select",
                                            "name": "type",
                                            "source": "get:/dnsRecord/getQTypeOptionList",
                                            "label": "类型",
                                            "required": true
                                        },
                                        {
                                            "type": "number",
                                            "name": "ttl",
                                            "label": "缓存时间(秒)",
                                            "required": true,
                                            "value": 600
                                        },
                                        {
                                            "type": "text",
                                            "name": "value",
                                            "label": "记录值",
                                            "required": true
                                        }
                                    ]
                                }
                            },
                        },
                        {
                            "label": "删除",
                            "type": "button",
                            "level": "danger",
                            "actionType": "ajax",
                            "api": "delete:/dnsRecord/delete?ids=${id}",
                            "confirmText": "确认删除吗?"
                        },
                    ]
                }
            ]
        }]
};
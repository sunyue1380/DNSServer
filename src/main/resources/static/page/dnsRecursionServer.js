let dnsRecursionServer = {
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
                    "api": "post:/dnsRecursionServer/add",
                    "controls": [
                        {
                            "type": "text",
                            "name": "dnsServerIP",
                            "label": "上级DNS服务IP",
                            "required": true
                        },
                    ],
                }
            },
        },
        {
            "type": "crud",
            "name": "dnsRecursionServerCrud",
            "api": "get:/dnsRecursionServer/getList",
            "quickSaveItemApi": "put:/dnsRecursionServer/update",
            "saveOrderApi": "get:/dnsRecursionServer/changeOrder?ids=${ids}",
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
                    "api": "delete:/dnsRecursionServer/delete?ids=${ids}",
                    "confirmText": "确定要批量删除吗?",
                },
            ],
            "draggable": true,
            "columns": [
                {
                    "name": "id",
                    "label": "ID",
                },
                {
                    "type": "text",
                    "name": "remark",
                    "label": "备注",
                    "quickEdit": {
                        "type": "text",
                        "saveImmediately": true
                    }
                },
                {
                    "type": "text",
                    "name": "dnsServerIP",
                    "label": "上级DNS服务IP",
                    "quickEdit": {
                        "type": "text",
                        "saveImmediately": true
                    }
                },
                {
                    "type": "text",
                    "name": "order",
                    "label": "优先顺序(越小越优先)",
                    "sortable": true
                },
                {
                    "type": "status",
                    "name": "enable",
                    "label": "是否启用",
                    "quickEdit": {
                        "mode": "inline",
                        "type": "switch",
                        "saveImmediately": true
                    }
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
                    "label": "操作",
                    "type": "operation",
                    'buttons': [
                        {
                            "label": "删除",
                            "type": "button",
                            "level": "danger",
                            "actionType": "ajax",
                            "api": "delete:/dnsRecursionServer/delete?ids=${id}",
                            "confirmText": "确认删除吗?"
                        },
                    ]
                }
            ]
        }]
};
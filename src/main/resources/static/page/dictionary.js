let dictionary = {
    "type": "page",
    "body": {
        "type": "crud",
        "api": "/dictionary/getList",
        "quickSaveItemApi": "put:/dictionary/update",
        "syncLocation": false,
        "affixHeader": false,
        "columns": [
            {
                "name": "id",
                "label": "ID"
            },
            {
                "name": "key",
                "label": "键"
            },
            {
                "name": "description",
                "label": "描述"
            },
            {
                "name": "value",
                "label": "值",
                "quickEdit": {
                    "type":"textarea",
                    "saveImmediately": true
                }
            },
            {
                "name": "createdTime",
                "label": "创建时间",
                "type":"datetime",
                "toggled": false
            },
            {
                "name": "updatedTime",
                "label": "更新时间",
                "type":"datetime",
                "toggled": false
            }
        ]
    }
};
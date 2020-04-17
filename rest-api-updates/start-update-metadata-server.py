from flask import Flask
from flask_restful import Api, Resource, reqparse

app = Flask(__name__)
api = Api(app)

updates = [
    {
        "number":"1",
        "date": 1012020,
        "file": "http://127.0.0.1:8000/files/colt-1.2.0.jar?dl=1",
        "description": "security patch 1"
    },
    {
        "number":"2",
        "date": 212020,
        "file": "http://127.0.0.1:8000/files/permazen-demo-4.1.5-sources.jar?dl=1",
        "description": "security patch 2"
    },
    {
        "number":"3",
        "date": 3012020,
        "file": "http://127.0.0.1:8000/files/hello.txt?dl=1",
        "description": "security patch 3"
    }
]

class Update(Resource):
    def get(self, number):
        for update in updates:
            if (number == update["number"]):
                return update, 200
        return "Update not found", 404

    def post(self, number):
        parser = reqparse.RequestParser()
        parser.add_argument("date")
        parser.add_argument("file")
        parser.add_argument("description")
        args = parser.parse_args()

        for update in updates:
            if (number == update["number"]):
                return "Update with number {} already exists".format(number), 400

        update = {
            "number": number,
            "date": args["date"],
            "file": args["file"],
            "description": args["description"]
        }
        updates.append(update)
        return update, 201

    def put(self, number):
        parser = reqparse.RequestParser()
        parser.add_argument("date")
        parser.add_argument("file")
        parser.add_argument("description")
        args = parser.parse_args()

        for update in updates:
            if (number == update["number"]):
                update["date"] = args["date"]
                update["file"] = args["file"]
                update["description"] = args["description"]
                return update, 200

        update = {
            "number": number,
            "date": args["date"],
            "file": args["file"],
            "description": args["description"]
        }
        updates.append(update)
        return update, 201

    def delete(self, number):
        global updates
        updates = [update for update in updates if update["number"] != number]
        return "{} is deleted.".formate(number), 200


api.add_resource(Update, "/api/update/<string:number>")

app.run(debug=True)

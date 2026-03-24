import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s',  target: 20},
        { duration: '1m', target: 50 },
        { duration: '30s', target: 0 },
    ]
};

export function setup()
{
    const uniqueId = Date.now();

    const registerRes = http.post(
        'http://localhost:8080/api/auth/register',
        JSON.stringify({
            "username": `k6user_${uniqueId}`,
            "password": "k6password",
            "email": `k6user_${uniqueId}@gmail.com`
        }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const loginRes = http.post(
        'http://localhost:8080/api/auth/login',
        JSON.stringify({
            "username": `k6user_${uniqueId}`,
            "password": "k6password"
        }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const token = loginRes.json().token;
    return { token: token };

}

export default function (data) {

    const createRes = http.post(
        'http://localhost:8080/api/tasks',
        JSON.stringify({
            "title": `test`,
            "description": "test",
            "status": "In Progress",
            "priority": 1,
            "assignee": "test"
        }),
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${data.token}`}}
    );

    check(createRes, {
        'create task returns 200': (r) => r.status === 200,
    });

    const taskId = createRes.json().id;

    sleep(1)

    const fetchRes = http.get(
        'http://localhost:8080/api/tasks',
        {headers: {'Authorization': `Bearer ${data.token}`}}
    );

    check(fetchRes, {
        'fetch task returns 200': (r) => r.status === 200,
    });

    sleep(1)

    const patchRes = http.patch(
        `http://localhost:8080/api/tasks/${taskId}`,
        JSON.stringify( {
            "status": "Completed"
        }),
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${data.token}`}}
    );

    check(patchRes, {
        'patch task returns 200': (r) => r.status === 200,
    });

    sleep(1)

    const deleteRes = http.del(
        `http://localhost:8080/api/tasks/${taskId}`,
        JSON.stringify( {}),
        {headers: {'Authorization': `Bearer ${data.token}`}}
    );

    check(deleteRes, {
        'delete task returns 200': (r) => r.status === 200,
    });
}
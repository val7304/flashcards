import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },
        { duration: '1m', target: 30 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
    const payload = JSON.stringify({
        name: `cat-${Math.random()}`,
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    // create categorie
    const resCat = http.post(`${BASE_URL}/api/categories`, payload, params);
    const catId = resCat.json('id');

    check(resCat, {
        'cat 201': (r) => r.status === 201,
    });

    http.get(`${BASE_URL}/api/categories`);
    sleep(1);

    //  create flashcard in this categorie
    const resFlash = http.post(`${BASE_URL}/api/flashcards`, {
        categoryId: catId,
        front: "question?", back: "réponse"
    }, params);

    check(resFlash, {
        'flashcard 201': r => r.status === 201
    });

    http.get(`${BASE_URL}/api/flashcards`);
    sleep(1);
}

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

    check(resCat, {
        'cat OK': (r) => r.status === 200 || r.status === 201,
    });

    // if OK → create flashcard
    if (resCat.status === 201) {
        const catId = resCat.json('id');

        const resFlash = http.post(`${BASE_URL}/api/flashcards`, {
            categoryId: catId,
            front: "question?",
            back: "réponse"
        }, params);

        check(resFlash, {
            'flashcard 201': r => r.status === 201
        });
    } else {
        console.log(`Catégorie échouée: ${resCat.status}`);
    }

    // GETs
    http.get(`${BASE_URL}/api/categories`);
    http.get(`${BASE_URL}/api/flashcards`);
    sleep(1);
}

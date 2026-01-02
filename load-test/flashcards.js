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
    console.log(`Cat status: ${resCat.status}`);
    console.log(`Cat body preview: ${resCat.body?.substring(0, 100)}`);

    check(resCat, {
        'cat OK': (r) => r.status === 200 || r.status === 201,
    });

    // Flashcard only if categorie OK
    if ((resCat.status === 200 || resCat.status === 201) && resCat.body) {
        try {
            const responseJson = resCat.json();
            console.log(`Full JSON: ${JSON.stringify(responseJson)}`);
            const catId = responseJson.id || responseJson.categoryId;
            console.log(`Extracted catId: "${catId}" (type: ${typeof catId})`);

            // Vérifie que catId est valide
            if (catId && catId !== null && catId !== undefined) {
                const resFlash = http.post(`${BASE_URL}/api/flashcards`, {
                    categoryId: catId,
                    front: "question?",
                    back: "réponse"
                }, params);

                check(resFlash, {
                    'flashcard OK': (r) => r.status === 200 || r.status === 201,
                });
            } else {
                console.log(`Skip flashcard: invalid catId (${catId})`);
            }
        } catch (e) {
            console.log(`JSON parse error: ${e}`);
        }
    } else {
        console.log(`Skip flashcard: cat status ${resCat.status}`);
    }

    // GETs
    http.get(`${BASE_URL}/api/categories`);
    http.get(`${BASE_URL}/api/flashcards`);
    sleep(1);
}
